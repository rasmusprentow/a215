
 // Thanks a lot to 3volution for helping me iron out some
 // bugs and for giving me some helpful suggestions.
 //
 // Thanks a lot to raa for helping me pinpoint the crash,
 // and discovering the respawn bug.
 //
 // Thanks a lot to VEN for being so smart.
 //
 // Thanks a lot to BAILOPAN for binary logging, and for
 // CSDM spawn files that I could leech off of.
 //
 // Thanks a lot to all of my supporters, predominantly:
 // 3volution, aligind4h0us3, arkshine, Curryking, Gunny,
 // IdiotSavant, Mordekay, polakpolak, raa, Silver Dragon,
 // and ToT | V!PER. (alphabetically)
 //
 // Thanks especially to all of the translators:
 // arkshine, b!orn, commonbullet, Curryking, Deviance,
 // D o o m, Fr3ak0ut, godlike, harbu, iggy_bus, jopmako,
 // KylixMynxAltoLAG, Morpheus759, SAMURAI16, TEG,
 // ToT | V!PER, Twilight Suzuka, and webpsiho. (alphabetically)

 #include <amxmodx>
 #include <amxmisc>
 #include <fakemeta>
 #include <fakemeta_util>
 #include <cstrike>

 // defines
 #define GG_VERSION		"1.17b"
 #define TOP_PLAYERS		10 // for !top10
 #define MAX_SPAWNS		128 // for gg_dm_spawn_random
 #define COMMAND_ACCESS		ADMIN_CVAR // for amx_gungame
 #define LANG_PLAYER_C		-76 // for gungame_print (arbitrary number)
 #define MAX_WEAPON_ORDERS	10 // for random gg_weapon_order
 #define TEMP_SAVES		32 // for gg_save_temp
 #define TNAME_SAVE		pev_noise3 // for blocking game_player_equip and player_weaponstrip

 // gg_status_display
 enum
 {
	STATUS_LEADERWPN = 1,
	STATUS_YOURWPN,
	STATUS_KILLSLEFT,
	STATUS_KILLSDONE
 };

 // toggle_gungame
 enum
 {
	TOGGLE_FORCE = -1,
	TOGGLE_DISABLE,
	TOGGLE_ENABLE
 };

 // bombStatus[3]
 enum
 {
	BOMB_PICKEDUP = -1,
	BOMB_DROPPED,
	BOMB_PLANTED
 };

 // cs_set_user_money
 #if cellbits == 32
    #define OFFSET_CSMONEY	115
 #else
    #define OFFSET_CSMONEY	140
 #endif
 #define OFFSET_LINUX		5

 // task ids
 #define TASK_END_STAR		200
 #define TASK_RESPAWN		300
 #define TASK_CHECK_RESPAWN	400
 #define TASK_CHECK_RESPAWN2	1100
 #define TASK_CLEAR_SAVE	500
 #define TASK_CHECK_DEATHMATCH	600
 #define TASK_REMOVE_PROTECTION 700
 #define TASK_TOGGLE_GUNGAME	800
 #define TASK_WARMUP_CHECK	900
 #define TASK_VERIFY_WEAPON	1000
 #define TASK_DELAYED_SUICIDE	1100
 #define TASK_REFRESH_NADE	1200

 // hud channels
 #define CHANNEL_STATUS		3
 #define CHANNEL_WARMUP		1

 // animations
 #define USP_DRAWANIM		6
 #define M4A1_DRAWANIM		5

 //
 // VARIABLE DEFINITIONS
 //

 // pcvar holders
 new gg_enabled, gg_ff_auto, gg_vote_setting, gg_map_setup, gg_join_msg,
 gg_weapon_order, gg_max_lvl, gg_triple_on, gg_turbo, gg_knife_pro,
 gg_worldspawn_suicide, gg_bomb_defuse_lvl, gg_handicap_on, gg_top10_handicap,
 gg_warmup_timer_setting, gg_knife_warmup, gg_nade_glock, gg_nade_smoke,
 gg_nade_flash, gg_sound_levelup, gg_sound_leveldown, gg_sound_nade,
 gg_sound_knife, gg_sound_welcome, gg_sound_triple, gg_sound_winner,
 gg_kills_per_lvl, gg_give_armor, gg_give_helmet, gg_vote_custom,
 gg_changelevel_custom, gg_ammo_amount, gg_dm, gg_dm_sp_time, gg_dm_sp_mode,
 gg_dm_spawn_random, gg_dm_spawn_delay, gg_stats_file, gg_stats_prune,
 gg_refill_on_kill, gg_colored_messages, gg_tk_penalty, gg_dm_corpses, gg_save_temp,
 gg_awp_oneshot, gg_host_touch_reward, gg_host_rescue_reward, gg_host_kill_reward,
 gg_dm_countdown, gg_status_display, gg_dm_spawn_afterplant, gg_block_objectives,
 gg_stats_mode, gg_pickup_others, gg_stats_winbonus, gg_map_iterations, gg_warmup_multi,
 gg_host_kill_penalty, gg_dm_start_random, gg_stats_ip, gg_extra_nades, gg_endmap_setup,
 gg_allow_changeteam, gg_autovote_rounds, gg_autovote_ratio, gg_autovote_delay,
 gg_autovote_time, gg_ignore_bots, gg_nade_refresh, gg_block_equips;

 // max clip
 stock const maxClip[31] = { -1, 13, -1, 10,  1,  7,  1,  30, 30,  1,  30,  20,  25, 30, 35, 25,  12,  20,
			10,  30, 100,  8, 30,  30, 20,  2,  7, 30, 30, -1,  50 };

 // max bpammo
 stock const maxAmmo[31] = { -1, 52, -1, 90, -1, 32, -1, 100, 90, -1, 120, 100, 100, 90, 90, 90, 100, 100,
			30, 120, 200, 32, 90, 120, 60, -1, 35, 90, 90, -1, 100 };

 // weapon slot lookup table
 stock const weaponSlots[] = { 0, 2, 0, 1, 4, 1, 5, 1, 1, 4, 2, 2, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1,
				1, 4, 2, 1, 1, 3, 1 };

 // misc
 new scores_menu, level_menu, bombMap, hostageMap, warmup = -1, len, voted, won, trailSpr, roundEnded,
 weaponOrder[416], menuText[512], dummy[2], tempSave[TEMP_SAVES][27], c4planter, czero, bombStatus[4], maxPlayers,
 gameStarted, mapIteration = 1, Float:spawns[MAX_SPAWNS][9], spawnCount, csdmSpawnCount, cfgDir[32],
 top10[TOP_PLAYERS][81], csrespawnEnabled, modName[12], autovoted, autovotes[2], roundsElapsed,
 gameCommenced, cycleNum = -1;

 // stats file stuff
 new sfFile[64], sfAuthid[24], sfWins[6], sfPoints[8], sfName[32], sfTimestamp[12], sfLineData[81];
 
 // event ids
 new gmsgSayText, gmsgCurWeapon, gmsgScenario, gmsgBombDrop, gmsgBombPickup, gmsgHideWeapon, gmsgCrosshair;

 // player values
 new level[33], levelsThisRound[33], score[33], weapon[33][24], star[33], welcomed[33],
 spawnProtected[33], blockResetHUD[33], page[33], blockCorpse[33], Float:lastDeathMsg[33], hosties[33][2],
 respawn_timeleft[33], Float:spawnTime[33], silenced[33], spawnSounds[33], oldTeam[33];

 //
 // INITIATION FUNCTIONS
 //

 native cs_respawn(index);

 // plugin load
 public plugin_init()
 {
	register_plugin("GunGame AMXX",GG_VERSION,"Avalanche");
	register_cvar("gg_version",GG_VERSION,FCVAR_SERVER);
	set_cvar_string("gg_version",GG_VERSION);

	// mehrsprachige unterstützung (nein, spreche ich nicht Deutsches)
	register_dictionary("gungame.txt");
	register_dictionary("common.txt");
	register_dictionary("adminvote.txt");

	// event ids
	gmsgSayText = get_user_msgid("SayText");
	gmsgCurWeapon = get_user_msgid("CurWeapon");
	gmsgScenario = get_user_msgid("Scenario");
	gmsgBombDrop = get_user_msgid("BombDrop");
	gmsgBombPickup = get_user_msgid("BombPickup");
	gmsgHideWeapon = get_user_msgid("HideWeapon");
	gmsgCrosshair = get_user_msgid("Crosshair");

	// forwards
	register_forward(FM_SetModel,"fw_setmodel");
	register_forward(FM_Touch,"fw_touch");
	register_forward(FM_EmitSound,"fw_emitsound");

	// events
	register_event("DeathMsg","event_deathmsg","a");
	register_event("ResetHUD","event_resethud","b");
	register_event("CurWeapon","event_curweapon","b","1=1");
	register_event("AmmoX","event_ammox","b");
	register_event("HLTV","event_new_round","a","1=0","2=0"); // VEN
	register_event("TextMsg","event_round_restart","a","2=#Game_Commencing","2=#Game_will_restart_in");
	register_event("30","event_intermission","a");
	register_event("23","event_bomb_detonation","a","1=17","6=-105","7=17"); // planted bomb exploded (before/after round end) event (discovered by Ryan)
	register_message(get_user_msgid("ClCorpse"),"message_clcorpse");
	register_message(get_user_msgid("Money"),"message_money");
	register_message(gmsgScenario,"message_scenario");
	register_message(gmsgBombDrop,"message_bombdrop");
	register_message(gmsgBombPickup,"message_bombpickup");
	register_message(get_user_msgid("WeapPickup"),"message_weappickup"); // for gg_block_objectives
	register_message(get_user_msgid("AmmoPickup"),"message_ammopickup"); // for gg_block_objectives
	register_message(get_user_msgid("TextMsg"),"message_textmsg"); // for gg_block_objectives
	register_message(get_user_msgid("HostagePos"),"message_hostagepos"); // for gg_block_objectives

	// logevents
	register_logevent("event_bomb_detonation",6,"3=Target_Bombed"); // another bomb exploded event, for security (VEN)
	register_logevent("logevent_bomb_planted",3,"2=Planted_The_Bomb"); // bomb planted (VEN)
	register_logevent("logevent_bomb_defused",3,"2=Defused_The_Bomb"); // bomb defused (VEN)
	register_logevent("logevent_round_end",2,"1=Round_End"); // round ended (VEN)
	register_logevent("logevent_hostage_touched",3,"2=Touched_A_Hostage");
	register_logevent("logevent_hostage_rescued",3,"2=Rescued_A_Hostage");
	register_logevent("logevent_hostage_killed",3,"2=Killed_A_Hostage");
	register_logevent("logevent_team_join",3,"1=joined team");

	// commands
	register_concmd("amx_gungame","cmd_gungame",ADMIN_CVAR,"<0|1> - toggles the functionality of GunGame");
	register_concmd("amx_gungame_level","cmd_gungame_level",ADMIN_BAN,"<target> <level> - sets target's level. use + or - for relative, otherwise it's absolute.");
	register_concmd("amx_gungame_vote","cmd_gungame_vote",ADMIN_VOTE,"- starts a vote to toggle GunGame");
	register_clcmd("fullupdate","cmd_fullupdate");
	register_clcmd("joinclass","cmd_joinclass"); // new menus
	register_menucmd(register_menuid("Terrorist_Select",1),511,"cmd_joinclass"); // old menus (thanks teame06)
	register_menucmd(register_menuid("CT_Select",1),511,"cmd_joinclass"); // old menus (thanks teame06)
	register_clcmd("say","cmd_say");
	register_clcmd("say_team","cmd_say");

	// menus
	register_menucmd(register_menuid("autovote_menu"),MENU_KEY_1|MENU_KEY_2|MENU_KEY_0,"autovote_menu_handler");
	register_menucmd(register_menuid("welcome_menu"),1023,"welcome_menu_handler");
	register_menucmd(register_menuid("restart_menu"),MENU_KEY_1|MENU_KEY_0,"restart_menu_handler");
	register_menucmd(register_menuid("weapons_menu"),MENU_KEY_1|MENU_KEY_2|MENU_KEY_0,"weapons_menu_handler");
	register_menucmd(register_menuid("top10_menu"),MENU_KEY_1|MENU_KEY_2|MENU_KEY_0,"top10_menu_handler");
	scores_menu = register_menuid("scores_menu");
	register_menucmd(scores_menu,MENU_KEY_1|MENU_KEY_2|MENU_KEY_0,"scores_menu_handler");
	level_menu = register_menuid("level_menu");
	register_menucmd(level_menu,1023,"level_menu_handler");

	// basic cvars
	gg_enabled = register_cvar("gg_enabled","1");
	gg_vote_setting = register_cvar("gg_vote_setting","1");
	gg_vote_custom = register_cvar("gg_vote_custom","");
	gg_changelevel_custom = register_cvar("gg_changelevel_custom","");
	gg_map_setup = register_cvar("gg_map_setup","mp_timelimit 45; mp_winlimit 0; sv_alltalk 0; mp_chattime 10; mp_c4timer 25");
	gg_endmap_setup = register_cvar("gg_endmap_setup","");
	gg_join_msg = register_cvar("gg_join_msg","1");
	gg_colored_messages = register_cvar("gg_colored_messages","1");
	gg_save_temp = register_cvar("gg_save_temp","300"); // = 5 * 60 = 5 minutes
	gg_status_display = register_cvar("gg_status_display","0");
	gg_map_iterations = register_cvar("gg_map_iterations","1");
	gg_ignore_bots = register_cvar("gg_ignore_bots","0");
	gg_block_equips = register_cvar("gg_block_equips","0");

	// autovote cvars
	gg_autovote_rounds = register_cvar("gg_autovote_rounds","0");
	gg_autovote_delay = register_cvar("gg_autovote_delay","8.0");
	gg_autovote_ratio = register_cvar("gg_autovote_ratio","0.51");
	gg_autovote_time = register_cvar("gg_autovote_time","10.0");

	// stats cvars
	gg_stats_file = register_cvar("gg_stats_file","gungame.stats");
	gg_stats_ip = register_cvar("gg_stats_ip","0");
	gg_stats_prune = register_cvar("gg_stats_prune","2592000"); // = 60 * 60 * 24 * 30 = 30 days
	gg_stats_mode = register_cvar("gg_stats_mode","1");
	gg_stats_winbonus = register_cvar("gg_stats_winbonus","1.5");

	// deathmatch cvars
	gg_dm = register_cvar("gg_dm","0");
	gg_dm_sp_time = register_cvar("gg_dm_sp_time","1.0");
	gg_dm_sp_mode = register_cvar("gg_dm_sp_mode","1");
	gg_dm_spawn_random = register_cvar("gg_dm_spawn_random","0");
	gg_dm_start_random = register_cvar("gg_dm_start_random","0");
	gg_dm_spawn_delay = register_cvar("gg_dm_spawn_delay","1.5");
	gg_dm_spawn_afterplant = register_cvar("gg_dm_spawn_afterplant","1");
	gg_dm_corpses = register_cvar("gg_dm_corpses","1");
	gg_dm_countdown = register_cvar("gg_dm_countdown","0");

	// objective cvars
	gg_block_objectives = register_cvar("gg_block_objectives","0");
	gg_bomb_defuse_lvl = register_cvar("gg_bomb_defuse_lvl","1");
	gg_host_touch_reward = register_cvar("gg_host_touch_reward","0");
	gg_host_rescue_reward = register_cvar("gg_host_rescue_reward","0");
	gg_host_kill_reward = register_cvar("gg_host_kill_reward","0");
	gg_host_kill_penalty = register_cvar("gg_host_kill_penalty","0");

	// gameplay cvars
	gg_ff_auto = register_cvar("gg_ff_auto","1");
	gg_weapon_order = register_cvar("gg_weapon_order","glock18,usp,p228,deagle,fiveseven,elite,m3,xm1014,tmp,mac10,mp5navy,ump45,p90,galil,famas,ak47,scout,m4a1,sg552,aug,m249,hegrenade,knife");
	gg_max_lvl = register_cvar("gg_max_lvl","3");
	gg_triple_on = register_cvar("gg_triple_on","0");
	gg_turbo = register_cvar("gg_turbo","0");
	gg_knife_pro = register_cvar("gg_knife_pro","0");
	gg_worldspawn_suicide = register_cvar("gg_worldspawn_suicide","1");
	gg_allow_changeteam = register_cvar("gg_allow_changeteam","0");
	gg_pickup_others = register_cvar("gg_pickup_others","0");
	gg_handicap_on = register_cvar("gg_handicap_on","1");
	gg_top10_handicap = register_cvar("gg_top10_handicap","1");
	gg_warmup_timer_setting = register_cvar("gg_warmup_timer_setting","60");
	gg_knife_warmup = register_cvar("gg_knife_warmup","1");
	gg_warmup_multi = register_cvar("gg_warmup_multi","0");
	gg_nade_glock = register_cvar("gg_nade_glock","0");
	gg_nade_smoke = register_cvar("gg_nade_smoke","0");
	gg_nade_flash = register_cvar("gg_nade_flash","0");
	gg_extra_nades = register_cvar("gg_extra_nades","0");
	gg_nade_refresh = register_cvar("gg_nade_refresh","0.0");
	gg_kills_per_lvl = register_cvar("gg_kills_per_lvl","1");
	gg_give_armor = register_cvar("gg_give_armor","100");
	gg_give_helmet = register_cvar("gg_give_helmet","1");
	gg_ammo_amount = register_cvar("gg_ammo_amount","200");
	gg_refill_on_kill = register_cvar("gg_refill_on_kill","1");
	gg_tk_penalty = register_cvar("gg_tk_penalty","0");
	gg_awp_oneshot = register_cvar("gg_awp_oneshot","1");

	// sound cvars
	gg_sound_levelup = register_cvar("gg_sound_levelup","gungame/smb3_powerup.wav");
	gg_sound_leveldown = register_cvar("gg_sound_leveldown","gungame/smb3_powerdown.wav");
	gg_sound_nade = register_cvar("gg_sound_nade","gungame/nade_level.wav");
	gg_sound_knife = register_cvar("gg_sound_knife","gungame/knife_level.wav");
	gg_sound_welcome = register_cvar("gg_sound_welcome","gungame/gungame2.wav");
	gg_sound_triple = register_cvar("gg_sound_triple","gungame/smb_star.wav");
	gg_sound_winner = register_cvar("gg_sound_winner","media/Half-Life08.mp3");

	// random weapon order cvars
	new i, cvar[20];
	for(i=1;i<=MAX_WEAPON_ORDERS;i++)
	{
		formatex(cvar,19,"gg_weapon_order%i",i);
		register_cvar(cvar,"");
	}

	// check for mini respawn module
	csrespawnEnabled = module_exists("csrespawn");

	// make sure to setup amx_nextmap incase nextmap.amxx isn't running
	if(!cvar_exists("amx_nextmap")) register_cvar("amx_nextmap","",FCVAR_SERVER|FCVAR_EXTDLL|FCVAR_SPONLY);

	// identify this as a bomb map
	if(fm_find_ent_by_class(1,"info_bomb_target") || fm_find_ent_by_class(1,"func_bomb_target"))
		bombMap = 1;

	// identify this as a hostage map
	if(fm_find_ent_by_class(1,"hostage_entity"))
		hostageMap = 1;

	get_modname(modName,11);

	// remember if we are running czero or not
	if(equali(modName,"czero")) czero = 1;

	maxPlayers = get_maxplayers();

	// grab CSDM file
	new mapName[32], csdmFile[64];
	get_configsdir(cfgDir,31);
	get_mapname(mapName,31);
	formatex(csdmFile,63,"%s/csdm/%s.spawns.cfg",cfgDir,mapName);

	// collect CSDM spawns
	if(file_exists(csdmFile))
	{
		new csdmData[10][6];

		new file = fopen(csdmFile,"rt");
		while(file && !feof(file))
		{
			fgets(file,sfLineData,63);

			// invalid spawn
			if(!sfLineData[0] || str_count(sfLineData,' ') < 2)
				continue;

			// BREAK IT UP!
			parse(sfLineData,csdmData[0],5,csdmData[1],5,csdmData[2],5,csdmData[3],5,csdmData[4],5,csdmData[5],5,csdmData[6],5,csdmData[7],5,csdmData[8],5,csdmData[9],5);

			// origin
			spawns[spawnCount][0] = floatstr(csdmData[0]);
			spawns[spawnCount][1] = floatstr(csdmData[1]);
			spawns[spawnCount][2] = floatstr(csdmData[2]);

			// angles
			spawns[spawnCount][3] = floatstr(csdmData[3]);
			spawns[spawnCount][4] = floatstr(csdmData[4]);
			spawns[spawnCount][5] = floatstr(csdmData[5]);

			// team, csdmData[6], unused

			// vangles
			spawns[spawnCount][6] = floatstr(csdmData[7]);
			spawns[spawnCount][7] = floatstr(csdmData[8]);
			spawns[spawnCount][8] = floatstr(csdmData[9]);

			spawnCount++;
			csdmSpawnCount++;
			if(spawnCount >= MAX_SPAWNS) break;
		}
		if(file) fclose(file);
	}

	// collect regular, boring spawns
	else
	{
		new ent, Float:spawnData[3];
		while((ent = fm_find_ent_by_class(ent,"info_player_deathmatch")) != 0)
		{
			// origin
			pev(ent,pev_origin,spawnData);
			spawns[spawnCount][0] = spawnData[0];
			spawns[spawnCount][1] = spawnData[1];
			spawns[spawnCount][2] = spawnData[2];

			// angles
			pev(ent,pev_angles,spawnData);
			spawns[spawnCount][3] = spawnData[0];
			spawns[spawnCount][4] = spawnData[1];
			spawns[spawnCount][5] = spawnData[2];

			// vangles
			spawns[spawnCount][6] = spawnData[0];
			spawns[spawnCount][7] = spawnData[1];
			spawns[spawnCount][8] = spawnData[2];

			spawnCount++;
			if(spawnCount >= MAX_SPAWNS) break;
		}

		// yeah, I probably should've optimized this

		ent = 0;
		while((ent = fm_find_ent_by_class(ent,"info_player_start")) != 0)
		{
			// origin
			pev(ent,pev_origin,spawnData);
			spawns[spawnCount][0] = spawnData[0];
			spawns[spawnCount][1] = spawnData[1];
			spawns[spawnCount][2] = spawnData[2];

			// angles
			pev(ent,pev_angles,spawnData);
			spawns[spawnCount][3] = spawnData[0];
			spawns[spawnCount][4] = spawnData[1];
			spawns[spawnCount][5] = spawnData[2];

			// vangles
			spawns[spawnCount][6] = spawnData[0];
			spawns[spawnCount][7] = spawnData[1];
			spawns[spawnCount][8] = spawnData[2];

			spawnCount++;
			if(spawnCount >= MAX_SPAWNS) break;
		}
	}

	// delay for server.cfg
	set_task(1.0,"toggle_gungame",TASK_TOGGLE_GUNGAME + TOGGLE_FORCE);

	// manage pruning (longer delay for toggle_gungame)
	set_task(2.0,"manage_pruning");
 }

 // plugin ends, prune stats file maybe
 public plugin_end()
 {
	// run endmap setup on plugin close
	if(get_pcvar_num(gg_enabled))
	{
		new setup[512];
		get_pcvar_string(gg_endmap_setup,setup,511);
		if(setup[0]) server_cmd(setup);
	}
 }

 // setup native filter for cs_respawn
 public plugin_natives()
 {
	set_native_filter("native_filter");
 }

 // don't throw error on cs_respawn if not running module
 public native_filter(const name[],index,trap)
 {
	if(equal(name,"cs_respawn"))
		return PLUGIN_HANDLED;

	return PLUGIN_CONTINUE;
 }

 // manage stats pruning
 public manage_pruning()
 {
	get_pcvar_string(gg_stats_file,sfFile,63);

	// stats disabled/file doesn't exist/pruning disabled
	if(!sfFile[0] || !file_exists(sfFile) || !get_pcvar_num(gg_stats_prune)) return;

	// get how many plugin ends more until we prune
	new prune_in_str[3], prune_in;
	get_localinfo("gg_prune_in",prune_in_str,2);
	prune_in = str_to_num(prune_in_str);

	// localinfo not set yet
	if(!prune_in)
	{
		set_localinfo("gg_prune_in","9");
		return;
	}

	// time to prune
	if(prune_in == 1)
	{
		// prune and log
		log_amx("%L",LANG_SERVER,"PRUNING",sfFile,stats_prune());

		// reset our prune count
		set_localinfo("gg_prune_in","10");
		return;
	}

	// decrement our count
	num_to_str(prune_in-1,prune_in_str,2);
	set_localinfo("gg_prune_in",prune_in_str);
 }

 // manage warmup mode
 public warmup_check(taskid)
 {
	warmup--;
	set_hudmessage(255,255,255,-1.0,0.4,0,6.0,1.0,0.1,0.2,CHANNEL_WARMUP);

	if(warmup <= 0)
	{
		warmup = -13;

		show_hudmessage(0,"%L",LANG_PLAYER,"WARMUP_ROUND_OVER");
		set_cvar_num("sv_restartround",1);
	}
	else
	{

		show_hudmessage(0,"%L",LANG_PLAYER,"WARMUP_ROUND_DISPLAY",warmup);
		set_task(1.0,"warmup_check",taskid);
	}
 }

 // plugin precache
 public plugin_precache()
 {
	precache_sound_by_cvar("gg_sound_levelup","gungame/smb3_powerup.wav");
	precache_sound_by_cvar("gg_sound_leveldown","gungame/smb3_powerdown.wav");
	precache_sound_by_cvar("gg_sound_nade","gungame/nade_level.wav");
	precache_sound_by_cvar("gg_sound_knife","gungame/knife_level.wav");
	precache_sound_by_cvar("gg_sound_welcome","gungame/gungame2.wav");
	precache_sound_by_cvar("gg_sound_triple","gungame/smb_star.wav");
	precache_sound_by_cvar("gg_sound_winner","media/Half-Life08.mp3");

	precache_sound("gungame/brass_bell_C.wav");
	precache_sound("buttons/bell1.wav");
	precache_sound("common/null.wav");

	trailSpr = precache_model("sprites/laserbeam.spr");
 }

 //
 // FORWARDS
 //

 // client gets a steamid
 public client_authorized(id)
 {
	clear_values(id);

	get_pcvar_string(gg_stats_file,sfFile,63);

	static authid[24];

	if(get_pcvar_num(gg_stats_ip)) get_user_ip(id,authid,23);
	else get_user_authid(id,authid,23);

	// refresh timestamp if we should
	if(sfFile[0]) stats_refresh_timestamp(authid);

	// load temporary save
	if(get_pcvar_num(gg_enabled) && get_pcvar_num(gg_save_temp))
	{
		new i, save = -1;

		// find our possible temp save
		for(i=0;i<TEMP_SAVES;i++)
		{
			if(equal(authid,tempSave[i],23))
			{
				save = i;
				break;
			}
		}

		// no temp save
		if(save == -1) return;

		// load values
		level[id] = tempSave[save][24];
		score[id] = tempSave[save][25];

		// clear it
		clear_save(TASK_CLEAR_SAVE+save);

		// get the name (almost forgot!)
		get_weapon_name_by_level(level[id],weapon[id],23);
	}
 }

 // client leaves, reset values
 public client_disconnect(id)
 {
	// remove certain tasks
	remove_task(TASK_RESPAWN+id);
	remove_task(TASK_CHECK_RESPAWN+id);
	remove_task(TASK_CHECK_RESPAWN2+id);
	remove_task(TASK_CHECK_DEATHMATCH+id);
	remove_task(TASK_REMOVE_PROTECTION+id);
	remove_task(TASK_VERIFY_WEAPON+id);
	remove_task(TASK_DELAYED_SUICIDE+id);
	remove_task(TASK_REFRESH_NADE+id);

	// clear bomb planter
	if(c4planter == id) c4planter = 0;

	// don't bother saving if in winning period
	if(!won)
	{
		new save_temp = get_pcvar_num(gg_save_temp);

		// temporarily save values
		if(get_pcvar_num(gg_enabled) && save_temp && (level[id] > 1 || score[id] > 0))
		{
			new freeSave = -1, oldestSave = -1, i;

			for(i=0;i<TEMP_SAVES;i++)
			{
				// we found a free one
				if(!tempSave[i][0])
				{
					freeSave = i;
					break;
				}

				// keep track of one soonest to expire
				if(oldestSave == -1 || tempSave[i][26] < tempSave[oldestSave][26])
					oldestSave = i;
			}

			// no free, use oldest
			if(freeSave == -1) freeSave = oldestSave;

			if(get_pcvar_num(gg_stats_ip)) get_user_ip(id,tempSave[freeSave],23);
			else get_user_authid(id,tempSave[freeSave],23);

			tempSave[freeSave][24] = level[id];
			tempSave[freeSave][25] = score[id];
			tempSave[freeSave][26] = floatround(get_gametime());

			set_task(float(save_temp),"clear_save",TASK_CLEAR_SAVE+freeSave);
		}
	}

	clear_values(id);
 }

 // remove a save
 public clear_save(taskid)
 {
	remove_task(taskid);
	tempSave[taskid-TASK_CLEAR_SAVE][0] = 0;
 }

 // clear all saved values
 stock clear_values(id,ignoreWelcome=0)
 {
	level[id] = 0;
	levelsThisRound[id] = 0;
	score[id] = 0;
	weapon[id][0] = 0;
	star[id] = 0;
	if(!ignoreWelcome) welcomed[id] = 0;
	spawnProtected[id] = 0;
	blockResetHUD[id] = 0;
	page[id] = 0;
	blockCorpse[id] = 0;
	respawn_timeleft[id] = 0;
	silenced[id] = 0;
	spawnSounds[id] = 1;
	oldTeam[id] = 0;
 }

 // an entity is given a model, stop weapons from
 // dropping in DM mode so that it doesn't get clustered
 public fw_setmodel(ent,model[])
 {
	if(!get_pcvar_num(gg_enabled) || !pev_valid(ent) || !model[0])
		return FMRES_IGNORED;

	new owner = pev(ent,pev_owner);

	// no owner
	if(!is_user_connected(owner)) return FMRES_IGNORED;

	static classname[24];
	pev(ent,pev_classname,classname,10);

	// not a weapon
	// checks for weaponbox, weapon_shield
	if(classname[8] != 'x' && !(classname[6] == '_' && classname[7] == 's' && classname[8] == 'h'))
		return FMRES_IGNORED;

	// makes sure we don't get memory access error,
	// but also helpful to narrow down matches
	new len = strlen(model);

	// ignore weaponboxes whose models haven't been set to correspond with their weapon types yet
	// checks for models/w_weaponbox.mdl
	if(len == 22 && model[17] == 'x') return FMRES_IGNORED;

	// C4 should always drop
	// checks for models/w_backpack.mdl
	if(len == 21 && model[9] == 'b') return FMRES_IGNORED;

	// if owner is dead
	if(get_user_health(owner) <= 0)
	{
		// first, remember silenced or burst status

		// checks for models/w_usp.mdl, usp, models/w_m4a1.mdl, m4a1
		if( (len == 16 && model[10] == 's' && weapon[owner][1] == 's')
		|| (len == 17 && model[10] == '4' && weapon[owner][1] == '4') )
		{
			copyc(model,len-1,model[containi(model,"_")+1],'.'); // strips off models/w_ and .mdl
			formatex(classname,23,"weapon_%s",model);

			new wEnt = fm_find_ent_by_owner(maxPlayers,classname,ent);
			if(pev_valid(wEnt)) silenced[owner] = cs_get_weapon_silen(wEnt);
		}

		// checks for models/w_glock18.mdl, glock18, models/w_famas.mdl, famas
		else if( (len == 20 && model[15] == '8' && weapon[owner][6] == '8')
		|| (len == 18 && model[9] == 'f' && model[10] == 'a' && weapon[owner][0] == 'f' && weapon[owner][1] == 'a') )
		{
			copyc(model,len-1,model[containi(model,"_")+1],'.'); // strips off models/w_ and .mdl
			formatex(classname,23,"weapon_%s",model);

			new wEnt = fm_find_ent_by_owner(maxPlayers,classname,ent);
			if(pev_valid(wEnt)) silenced[owner] = cs_get_weapon_burst(wEnt);
		}

		if(get_pcvar_num(gg_dm) && !get_pcvar_num(gg_pickup_others))
		{
			// then remove it
			dllfunc(DLLFunc_Think,ent);
		}
	}

	return FMRES_IGNORED;
 }

 // touchy touchy
 public fw_touch(touched,toucher)
 {
	// invalid entities involved or gungame disabled
	if(!get_pcvar_num(gg_enabled) || !pev_valid(touched) || !is_user_connected(toucher))
		return FMRES_IGNORED;

	static classname[10];
	pev(touched,pev_classname,classname,9);

	// not touching a weapon-giver
	// checks for weaponbox, weapon_*, armoury_*
	if(classname[8] != 'x'
	&& !(classname[0] == 'w' && classname[1] == 'e' && classname[2] == 'a')
	&& !(classname[0] == 'a' && classname[1] == 'r' && classname[2] == 'm'))
		return FMRES_IGNORED;

	// removing starting pistols
	if(fm_halflife_time() - spawnTime[toucher] < 0.2)
	{
		dllfunc(DLLFunc_Think,touched);
		return FMRES_SUPERCEDE;
	}

	static model[24];
	pev(touched,pev_model,model,23);

	// not touching the kind of weaponbox that we like
	// checks for models/w_weaponbox.mdl
	if(model[17] == 'x') return FMRES_IGNORED;

	// allow pickup of C4
	// checks for models/w_backpack.mdl
	if(model[9] == 'b') return FMRES_IGNORED;

	// knife warmup, no weapons allowed
	if(warmup > 0 && get_pcvar_num(gg_knife_warmup))
		return FMRES_SUPERCEDE;

	// we are allowed to pick up other weapons
	if(get_pcvar_num(gg_pickup_others))
		return FMRES_IGNORED;

	// weapon is weapon_mp5navy, but model is w_mp5.mdl
	// checks for models/w_mp5.mdl
	if(model[10] == 'p' && model[11] == '5') model = "mp5navy";

	// get the type of weapon based on model
	else
	{
		replace(model,23,"models/w_","");
		replace(model,23,".mdl","");
	}

	// everyone is allowed to use knife
	// checks for knife
	if(model[0] == 'k') return FMRES_IGNORED;

	// check hegrenade exceptions
	// checks for hegrenade
	if(weapon[toucher][0] == 'h')
	{
		if(model[6] == '8' && get_pcvar_num(gg_nade_glock)) return FMRES_IGNORED; // glock18
		if(model[0] == 's' && model[1] == 'm' && get_pcvar_num(gg_nade_smoke)) return FMRES_IGNORED; // smokegrenade
		if(model[0] == 'f' && model[1] == 'l' && get_pcvar_num(gg_nade_flash)) return FMRES_IGNORED; // flashbang
	}

	// this is our weapon, don't mess with it
	if(equal(weapon[toucher],model)) return FMRES_IGNORED;

	// otherwise, this is an item we're not allowed to have
	return FMRES_SUPERCEDE;
 }

 // HELLO HELLo HELlo HEllo Hello hello
 public fw_emitsound(ent,channel,sample[],Float:volume,Float:atten,flags,pitch)
 {
	if(!get_pcvar_num(gg_enabled) || !is_user_connected(ent) || !get_pcvar_num(gg_dm) || spawnSounds[ent])
		return FMRES_IGNORED;

	return FMRES_SUPERCEDE;
 }

 //
 // EVENT HOOKS
 //

 // respawnish
 public event_resethud(id)
 {
	if(!get_pcvar_num(gg_enabled) || !is_user_connected(id))
		return;

	remove_task(TASK_CHECK_DEATHMATCH+id);

	// ignore first ResetHUD after sv_restartround
	if(blockResetHUD[id])
	{
		set_task(0.1,"hide_money",id);
		blockResetHUD[id]--;
		return;
	}

	// have not joined yet
	new CsTeams:team = cs_get_user_team(id);
	if(team != CS_TEAM_T && team != CS_TEAM_CT) return;

	spawnTime[id] = fm_halflife_time();

	status_display(id);
	set_task(0.1,"post_resethud",id);
 }

 // our delay
 public post_resethud(id)
 {
	if(!is_user_connected(id) || pev(id,pev_iuser1)) return;

	levelsThisRound[id] = 0;

	// just joined
	if(!level[id])
	{
		// handicap
		new handicapMode = get_pcvar_num(gg_handicap_on);
		if(handicapMode)
		{
			new rcvHandicap = 1;

			get_pcvar_string(gg_stats_file,sfFile,63);

			// top10 doesn't receive handicap -- also make sure we are using top10
			if(!get_pcvar_num(gg_top10_handicap) && sfFile[0] && file_exists(sfFile) && get_pcvar_num(gg_stats_mode))
			{
				static authid[24];

				if(get_pcvar_num(gg_stats_ip)) get_user_ip(id,authid,23);
				else get_user_authid(id,authid,23);

				new i;
				for(i=0;i<TOP_PLAYERS;i++)
				{
					// blank
					if(!top10[i][0]) continue;

					// isolate authid
					strtok(top10[i],sfAuthid,23,dummy,1,'^t');

					// I'm in top10, don't give me handicap
					if(equal(authid,sfAuthid))
					{
						rcvHandicap = 0;
						break;
					}
				}
			}

			if(rcvHandicap)
			{
				new players[32], num, i;
				get_players(players,num);

				// find lowest level (don't use bots unless we have to)
				if(handicapMode == 2)
				{
					new isBot, myLevel, lowestLevel, lowestBotLevel;
					for(i=0;i<num;i++)
					{
						if(players[i] == id)
							continue;

						isBot = is_user_bot(players[i]);
						myLevel = level[players[i]];

						if(!myLevel) continue;

						if(!isBot && (!lowestLevel || myLevel < lowestLevel))
							lowestLevel = myLevel;
						else if(isBot && (!lowestBotLevel || myLevel < lowestBotLevel))
							lowestBotLevel = myLevel;
					}

					// CLAMP!
					if(!lowestLevel) lowestLevel = 1;
					if(!lowestBotLevel) lowestBotLevel = 1;

					change_level(id,(lowestLevel > 1) ? lowestLevel : lowestBotLevel,1,0);
				}

				// find average level
				else
				{
					new Float:average;
					for(i=0;i<num;i++)
					{
						if(players[i] != id)
							average += float(level[players[i]]);
					}

					average /= float(num)-1;
					change_level(id,(average >= 0.5) ? floatround(average) : 1,1,0);
				}
			}

			// not eligible for handicap (in top10 with gg_top10_handicap disabled)
			else change_level(id,1,1,0);
		}

		// no handicap enabled
		else change_level(id,1,1,0);

		give_level_weapon(id);
	}

	// didn't just join
	else
	{
		if(star[id])
		{
			end_star(TASK_END_STAR+id);
			remove_task(TASK_END_STAR+id);
		}

		get_weapon_name_by_level(level[id],weapon[id],23);
		give_level_weapon(id);
		refill_ammo(id);
	}

	// show welcome message
	if(!welcomed[id] && get_pcvar_num(gg_join_msg))
		show_welcome(id);

	// update bomb for DM
	if(cs_get_user_team(id) == CS_TEAM_T && !get_pcvar_num(gg_block_objectives) && get_pcvar_num(gg_dm))
	{

		if(bombStatus[3] == BOMB_PICKEDUP)
		{
			message_begin(MSG_ONE,gmsgBombPickup,_,id);
			message_end();
		}
		else if(bombStatus[0] || bombStatus[1] || bombStatus[2])
		{
			message_begin(MSG_ONE,gmsgBombDrop,_,id);
			write_coord(bombStatus[0]);
			write_coord(bombStatus[1]);
			write_coord(bombStatus[2]);
			write_byte(bombStatus[3]);
			message_end();
		}
	}

	hide_money(id);
 }

 // hide someone's money display
 public hide_money(id)
 {
	// hide money
	message_begin(MSG_ONE,gmsgHideWeapon,_,id);
	write_byte(1<<5);
	message_end();

	// hide crosshair that appears from hiding money
	message_begin(MSG_ONE,gmsgCrosshair,_,id);
	write_byte(0);
	message_end();
 }

 // someone has been killed
 public event_deathmsg()
 {
	if(!get_pcvar_num(gg_enabled)) return;

	new killer = read_data(1);
	new victim = read_data(2);

	new Float:time = fm_halflife_time();

	// a bug with valve's code (dvander said so!):
	// sometimes, when using an item giving code
	// (as that's where this can lead) within a message
	// hook, the message hook can get called twice.
	// so, if this player "dies" twice within X seconds,
	// it's obviously a bugged message, so ignore.
	if(time - lastDeathMsg[victim] < 0.2)
		return;

	lastDeathMsg[victim] = time;

	remove_task(TASK_VERIFY_WEAPON+victim);

	star[victim] = 0;
	remove_task(TASK_END_STAR+victim);

	// allow us to join in on deathmatch
	if(!get_pcvar_num(gg_dm))
	{
		remove_task(TASK_CHECK_DEATHMATCH+victim);
		set_task(10.0,"check_deathmatch",TASK_CHECK_DEATHMATCH+victim);
	}

	// respawn us
	else
	{
		remove_task(TASK_RESPAWN+victim);
		remove_task(TASK_CHECK_RESPAWN+victim);
		remove_task(TASK_CHECK_RESPAWN2+victim);
		remove_task(TASK_REMOVE_PROTECTION+victim);
		set_task(0.1,"begin_respawn",victim);
		fm_set_user_rendering(victim); // clear spawn protection
	}

	// stops defusal kits from dropping in deathmatch mode
	if(bombMap && get_pcvar_num(gg_dm)) cs_set_user_defuse(victim,0);

	// remember victim's silenced status
	if(equal(weapon[victim],"usp") || equal(weapon[victim],"m4a1"))
	{
		new wEnt = get_weapon_ent(victim,_,weapon[victim]);
		if(pev_valid(wEnt)) silenced[victim] = cs_get_weapon_silen(wEnt);
	}

	// or, remember burst status
	else if(equal(weapon[victim],"glock18") || equal(weapon[victim],"famas"))
	{
		new wEnt = get_weapon_ent(victim,_,weapon[victim]);
		if(pev_valid(wEnt)) silenced[victim] = cs_get_weapon_burst(wEnt);
	}

	// NOTE: simply calling fm_remove_entity on defusal kits after they are dropped
	// has the potential to cause a crash. the crash was pinpointed with the help of
	// raa, and the solution was offered by VEN.

	static wpnName[24];
	read_data(4,wpnName,23);

	// deaths by hegrenade are reported as grenade, fix
	if(wpnName[0] == 'g' && wpnName[1] == 'r' && wpnName[2] == 'e')
		wpnName = "hegrenade";

	// killed self with worldspawn
	if(equal(wpnName,"worldspawn"))
	{
		if(get_pcvar_num(gg_worldspawn_suicide)) player_suicided(victim);
		return;
	}


	// killed self not with worldspawn
	if(killer == victim)
	{
		if(!roundEnded && get_pcvar_num(gg_allow_changeteam) && equal(wpnName,"world"))
		{
			oldTeam[victim] = get_user_team(victim);
			set_task(0.1,"delayed_suicide",TASK_DELAYED_SUICIDE+victim);
		}
		else player_suicided(victim);

		return;
	}

	// team kill
	if(get_user_team(killer) == get_user_team(victim))
	{
		new penalty = get_pcvar_num(gg_tk_penalty);

		if(penalty > 0)
		{
			new name[32];
			get_user_name(killer,name,31);

			if(score[killer] - penalty < 0)
				gungame_print(0,killer,"%L",LANG_PLAYER_C,"TK_LEVEL_DOWN",name,(level[killer] > 1) ? level[killer]-1 : level[killer]);
			else
				gungame_print(0,killer,"%L",LANG_PLAYER_C,"TK_SCORE_DOWN",name,penalty);

			change_score(killer,-penalty);
		}

		return;
	}

	// other player had spawn protection
	if(spawnProtected[victim])
	{
		new name[32];
		get_user_name(victim,name,31);

		gungame_print(killer,victim,"%L",killer,"SPAWNPROTECTED_KILL",name,floatround(get_pcvar_float(gg_dm_sp_time)));
		return;
	}

	new canLevel = 1, scored;

	// already reached max levels this round
	new max_lvl = get_pcvar_num(gg_max_lvl);
	if(!get_pcvar_num(gg_turbo) && max_lvl > 0 && levelsThisRound[killer] >= max_lvl)
		canLevel = 0;

	// was it a knife kill, and does it matter?
	if(equal(wpnName,"knife") && get_pcvar_num(gg_knife_pro) && !equal(weapon[killer],"knife"))
	{
		// knife warmup, don't bother leveling up
		if(warmup > 0 && get_pcvar_num(gg_knife_warmup)) return;

		static killerName[32], victimName[32];
		get_user_name(killer,killerName,31);
		get_user_name(victim,victimName,31);

		gungame_print(0,killer,"%L",LANG_PLAYER_C,"STOLE_LEVEL",killerName,victimName);

		if(canLevel && !equal(weapon[killer],"hegrenade"))
		{
			if(level[killer] < get_weapon_num()) change_level(killer,1);
		}

		if(level[victim] > 1) change_level(victim,-1);
	}

	// otherwise, if he killed with his appropiate weapon, give him a point
	else if(canLevel && equal(weapon[killer],wpnName))
	{
		scored = 1;

		// didn't level off of it
		if(!change_score(killer,1)) show_required_kills(killer);
	}

	// award for killing hostage carrier
	new host_kill_reward = get_pcvar_num(gg_host_kill_reward);

	// note that this doesn't work with CZ hostages
	if(hostageMap && !czero && host_kill_reward && !equal(weapon[killer],"hegrenade") && !equal(weapon[killer],"knife"))
	{
		// check for hostages following this player
		new hostage;
		while((hostage = fm_find_ent_by_class(hostage,"hostage_entity")) != 0)
		{
			if(cs_get_hostage_foll(hostage) == victim && pev(hostage,pev_deadflag) == DEAD_NO)
				break;
		}

		// award bonus score if victim had hostages
		if(hostage)
		{
			scored = 1;

			if(!equali(weapon[killer],"hegrenade") && !equali(weapon[killer],"knife") && level[killer] < get_weapon_num())
			{
				// didn't level off of it
				if(!change_score(killer,host_kill_reward) || score[killer])
					show_required_kills(killer);
			}
		}
	}

	if(equal(weapon[killer],"hegrenade") && get_pcvar_num(gg_extra_nades) && !cs_get_user_bpammo(killer,CSW_HEGRENADE))
	{
		fm_give_item(killer,"weapon_hegrenade");
		remove_task(TASK_REFRESH_NADE+killer);
	}

	if((!scored || !get_pcvar_num(gg_turbo)) && get_pcvar_num(gg_refill_on_kill))
		refill_ammo(killer,1);
 }

 // task is set on a potential team change, and removed on an
 // approved team change, so if we reach it, deduct level
 public delayed_suicide(taskid)
 {
	new id = taskid-TASK_DELAYED_SUICIDE;

	oldTeam[id] = 0;
	if(is_user_connected(id)) player_suicided(id);
 }

 // someone changes weapons
 public event_curweapon(id)
 {
	if(!get_pcvar_num(gg_enabled)) return;

	// keep star speed
	if(star[id]) fm_set_user_maxspeed(id,fm_get_user_maxspeed(id)*1.5);

	// have only 1 ammo in awp clip
	if(get_pcvar_num(gg_awp_oneshot) && read_data(2) == CSW_AWP && read_data(3) > 1)
	{
		new wEnt = get_weapon_ent(id,CSW_AWP);
		if(pev_valid(wEnt)) cs_set_weapon_ammo(wEnt,1);

		message_begin(MSG_ONE,gmsgCurWeapon,_,id);
		write_byte(1);
		write_byte(CSW_AWP);
		write_byte(1);
		message_end();
	}
 }

 // ammo amount changes
 public event_ammox(id)
 {
	new type = read_data(1);

	// not HE grenade ammo, or not on the grenade level
	if(type != 12 || !equali(weapon[id],"hegrenade")) return;

	new amount = read_data(2);

	// still have some left, ignore
	if(amount > 0)
	{
		remove_task(TASK_REFRESH_NADE+id);
		return;
	}

	new Float:refresh = get_pcvar_float(gg_nade_refresh);

	// refreshing is disabled, or we are already giving one out
	if(refresh <= 0.0 || task_exists(TASK_REFRESH_NADE+id)) return;

	// start the timer for the new grenade
	set_task(refresh,"refresh_nade",TASK_REFRESH_NADE+id);
 }

 // a new round has begun
 public event_new_round()
 {
	roundsElapsed++;

	if(!autovoted)
	{
		new autovote_rounds = get_pcvar_num(gg_autovote_rounds);

		if(autovote_rounds && gameCommenced && roundsElapsed >= autovote_rounds)
		{
			autovoted = 1;
			set_task(get_pcvar_float(gg_autovote_delay),"autovote_start");
		}
	}

	// game_player_equip
	manage_equips();

	if(!get_pcvar_num(gg_enabled)) return;

	roundEnded = 0;
	c4planter = 0;
	bombStatus[3] = BOMB_PICKEDUP;

	// block hostages
	if(hostageMap && get_pcvar_num(gg_block_objectives))
		set_task(0.1,"move_hostages");

	new i;
	for(i=0;i<33;i++)
	{
		hosties[i][0] = 0;
		hosties[i][1] = 0;
	}

	new leader = get_leader(dummy[0]);

	if(equal(weapon[leader],"hegrenade")) play_sound_by_cvar(0,gg_sound_nade);
	else if(equal(weapon[leader],"knife")) play_sound_by_cvar(0,gg_sound_knife);

	// start in random positions at round start
	if(get_pcvar_num(gg_dm) && get_pcvar_num(gg_dm_start_random))
		set_task(0.1,"randomly_place_everyone");
 }

 // what do you think??
 public randomly_place_everyone()
 {
	new players[32], num, i, CsTeams:team;
	get_players(players,num);

	// count number of legitimate players
	new validNum;
	for(i=0;i<num;i++)
	{
		team = cs_get_user_team(players[i]);
		if(team == CS_TEAM_T || team == CS_TEAM_CT) validNum++;
	}

	// not enough CSDM spawns for everyone
	if(validNum > csdmSpawnCount)
		return;

	// now randomly place them
	for(i=0;i<num;i++)
	{
		team = cs_get_user_team(players[i]);

		// not spectator or unassigned
		if(team == CS_TEAM_T || team == CS_TEAM_CT)
			do_random_spawn(players[i],2);
	}
 }

 // manage game_player_equip and player_weaponstrip entities
 public manage_equips()
 {
	static classname[20], targetname[256];
	new ent, i, block_equips = get_pcvar_num(gg_block_equips), enabled = get_pcvar_num(gg_enabled);

	// go through both entities to monitor
	for(i=0;i<2;i++)
	{
		// get classname for current iteration
		switch(i)
		{
			case 0: classname = "game_player_equip";
			default: classname = "player_weaponstrip";
		}

		// go through whatever entity
		while((ent = fm_find_ent_by_class(ent,classname)) != 0)
		{
			pev(ent,pev_targetname,targetname,255);

			// allowed to have this, reverse possible changes
			if(!enabled || !block_equips || (i == 1 && block_equips < 2)) // player_weaponstrip switch
			{
				// this one was blocked
				if(equali(targetname,"gg_block_equips"))
				{
					pev(ent,TNAME_SAVE,targetname,255);

					set_pev(ent,pev_targetname,targetname);
					set_pev(ent,TNAME_SAVE,"");
				}
			}

			// not allowed to pickup others, make possible changes
			else
			{
				pev(ent,pev_targetname,targetname,255);

				// needs to be blocked, but hasn't been yet
				if(targetname[0] && !equali(targetname,"gg_block_equips"))
				{
					set_pev(ent,TNAME_SAVE,targetname);
					set_pev(ent,pev_targetname,"gg_block_equips");
				}
			}
		}
	}
 }

 // move the hostages so that CTs can't get to them
 public move_hostages()
 {
	new ent;
	while((ent = fm_find_ent_by_class(ent,"hostage_entity")) != 0)
		set_pev(ent,pev_origin,Float:{8192.0,8192.0,8192.0});
 }

 // round is restarting (TAG: sv_restartround)
 public event_round_restart()
 {
	static message[32];
	read_data(2,message,31);

	if(equal(message,"#Game_Commencing")) gameCommenced = 1;

	if(!get_pcvar_num(gg_enabled)) return;

	new block;

	// block only on round restart, not game commencing
	if(equal(message,"#Game_will_restart_in")) block = 1;

	// don't reset values on game commencing,
	// if it has already commenced once
	else
	{
		if(gameStarted) return;
		gameStarted = 1;
	}

	new players[32], num, i;
	get_players(players,num);

	for(i=0;i<num;i++)
	{
		clear_values(players[i],1);

		// bots don't experience that ResetHUD on round restart
		if(!is_user_bot(players[i]) && block) blockResetHUD[players[i]]++;
	}
 }

 // map is changing
 public event_intermission()
 {
	if(!get_pcvar_num(gg_enabled) || won) return;

	// grab highest level
	new leaderLevel;
	get_leader(leaderLevel);

	// grab player list
	new players[32], pNum, winner, i;
	get_players(players,pNum);

	new topLevel[32], tlNum;

	// get all of the highest level players
	for(i=0;i<pNum;i++)
	{
		if(level[players[i]] == leaderLevel)
			topLevel[tlNum++] = players[i];
	}

	// only one on top level
	if(tlNum == 1) winner = topLevel[0];
	else
	{
		new highestKills, frags;

		// get the most kills
		for(i=0;i<tlNum;i++)
		{
			frags = get_user_frags(topLevel[i]);

			if(frags >= highestKills)
				highestKills = frags;
		}

		new topKillers[32], tkNum;

		// get all of the players with highest kills
		for(i=0;i<tlNum;i++)
		{
			if(get_user_frags(topLevel[i]) == highestKills)
				topKillers[tkNum++] = topLevel[i];
		}

		// only one on top kills
		if(tkNum == 1) winner = topKillers[0];
		else
		{
			new leastDeaths, deaths;

			// get the least deaths
			for(i=0;i<tkNum;i++)
			{
				deaths = cs_get_user_deaths(topKillers[i]);

				if(deaths <= leastDeaths)
					leastDeaths = deaths;
			}

			new leastDead[32], ldNum;

			// get all of the players with lowest deaths
			for(i=0;i<tkNum;i++)
			{
				if(cs_get_user_deaths(topKillers[i]) == leastDeaths)
					leastDead[ldNum++] = topKillers[i];
			}

			leastDead[random_num(0,ldNum-1)];
		}
	}

	// crown them
	win(winner);
 }

 // the bomb explodes
 public event_bomb_detonation()
 {
	if(!get_pcvar_num(gg_enabled) || get_pcvar_num(gg_bomb_defuse_lvl) != 2 || !c4planter)
		return;

	new id = c4planter;
	c4planter = 0;

	if(!is_user_connected(id)) return;

	if(!equal(weapon[id],"hegrenade") && !equal(weapon[id],"knife") && level[id] < get_weapon_num())
	{
		change_level(id,1);
		//score[id] = 0;
	}
	else if(is_user_alive(id)) refill_ammo(id);
 }

 // scenario changes
 public message_scenario(msg_id,msg_dest,msg_entity)
 {
	// disabled
	if(!get_pcvar_num(gg_enabled))
		return PLUGIN_CONTINUE;

	// don't override our custom display, if we have one
	if(get_pcvar_num(gg_status_display))
		return PLUGIN_HANDLED;

	// block hostage display if we disabled objectives
	else if(get_msg_args() > 1 && get_pcvar_num(gg_block_objectives))
	{
		new sprite[8];
		get_msg_arg_string(2,sprite,7);

		if(equal(sprite,"hostage"))
			return PLUGIN_HANDLED;
	}

	return PLUGIN_CONTINUE;
 }

 // bomb is dropped, remember for DM
 public message_bombdrop(msg_id,msg_dest,msg_entity)
 {
	if(get_pcvar_num(gg_enabled) && get_pcvar_num(gg_block_objectives))
		return PLUGIN_HANDLED;

	// you can't simply get_msg_arg_int the coords
	bombStatus[0] = floatround(get_msg_arg_float(1));
	bombStatus[1] = floatround(get_msg_arg_float(2));
	bombStatus[2] = floatround(get_msg_arg_float(3));
	bombStatus[3] = get_msg_arg_int(4);

	return PLUGIN_CONTINUE;
 }

 // bomb is picked up, remember for DM
 public message_bombpickup(msg_id,msg_dest,msg_entity)
 {
	bombStatus[3] = BOMB_PICKEDUP;
	return PLUGIN_CONTINUE;
 }

 // money money money!
 public message_money(msg_id,msg_dest,msg_entity)
 {
	if(!get_pcvar_num(gg_enabled) || !is_user_connected(msg_entity) || !is_user_alive(msg_entity))
		return PLUGIN_CONTINUE;

	// this now just changes the value of the message, passes it along,
	// and then modifies the pdata, instead of calling another cs_set_user_money
	// and sending out more messages than needed.

	set_msg_arg_int(1,ARG_LONG,0); // money
	set_msg_arg_int(2,ARG_BYTE,0); // flash

	set_pdata_int(msg_entity,OFFSET_CSMONEY,0,OFFSET_LINUX);
	return PLUGIN_CONTINUE;
 }

 // a corpse is to be set, stop player shells bug (thanks sawce)
 public message_clcorpse(msg_id,msg_dest,msg_entity)
 {
	if(!get_pcvar_num(gg_enabled) || get_msg_args() < 12)
		return PLUGIN_CONTINUE;

	if((get_pcvar_num(gg_dm) && !get_pcvar_num(gg_dm_corpses)) || blockCorpse[get_msg_arg_int(12)])
		return PLUGIN_HANDLED;

	return PLUGIN_CONTINUE;
 }

 // remove c4 if we disabled objectives
 public message_weappickup(msg_id,msg_dest,msg_entity)
 {
	if(!bombMap || !get_pcvar_num(gg_enabled) || !get_pcvar_num(gg_block_objectives))
		return PLUGIN_CONTINUE;

	if(get_msg_arg_int(1) == CSW_C4)
	{
		set_task(0.1,"strip_c4",msg_entity);
		return PLUGIN_HANDLED;
	}

	return PLUGIN_CONTINUE;
 }

 // delay, since weappickup is slightly before we actually get the weapon
 public strip_c4(id)
 {
	fm_strip_user_gun(id,CSW_C4);
 }

 // block c4 ammo message if we disabled objectives
 public message_ammopickup(msg_id,msg_dest,msg_entity)
 {
	if(!bombMap || !get_pcvar_num(gg_enabled) || !get_pcvar_num(gg_block_objectives))
		return PLUGIN_CONTINUE;

	if(get_msg_arg_int(1) == 14) // C4
		return PLUGIN_HANDLED;

	return PLUGIN_CONTINUE;
 }

 // block dropped the bomb message if we disabled objectives
 public message_textmsg(msg_id,msg_dest,msg_entity)
 {
	if(!bombMap || !get_pcvar_num(gg_enabled) || !get_pcvar_num(gg_block_objectives))
		return PLUGIN_CONTINUE;

	static message[16];
	get_msg_arg_string(2,message,15);

	if(equal(message,"#Game_bomb_drop"))
		return PLUGIN_HANDLED;

	return PLUGIN_CONTINUE;
 }

 // block hostages from appearing on radar if we disabled objectives
 public message_hostagepos(msg_id,msg_dest,msg_entity)
 {
	if(!get_pcvar_num(gg_enabled) || !get_pcvar_num(gg_block_objectives))
		return PLUGIN_CONTINUE;

	return PLUGIN_HANDLED;
 }

 //
 // LOG EVENT HOOKS
 //

 // someone planted the bomb
 public logevent_bomb_planted()
 {
	if(!get_pcvar_num(gg_enabled) || !get_pcvar_num(gg_bomb_defuse_lvl) || roundEnded)
		return;

	new id = get_loguser_index();
	if(!is_user_connected(id)) return;

	if(get_pcvar_num(gg_bomb_defuse_lvl) == 2) c4planter = id;
	else
	{
		if(!equal(weapon[id],"hegrenade") && !equal(weapon[id],"knife") && level[id] < get_weapon_num())
		{
			change_level(id,1);
			//score[id] = 0;
		}
		else refill_ammo(id);
	}

 }

 // someone defused the bomb
 public logevent_bomb_defused()
 {
	if(!get_pcvar_num(gg_enabled) || !get_pcvar_num(gg_bomb_defuse_lvl))
		return;

	new id = get_loguser_index();
	if(!is_user_connected(id)) return;

	if(!equal(weapon[id],"hegrenade") && !equal(weapon[id],"knife") && level[id] < get_weapon_num())
	{
		change_level(id,1);
		//score[id] = 0;
	}
	else refill_ammo(id);
 }

 // the round ends
 public logevent_round_end()
 {
	roundEnded = 1;
 }

 // hostage is touched
 public logevent_hostage_touched()
 {
	new reward = get_pcvar_num(gg_host_touch_reward);

	if(!get_pcvar_num(gg_enabled) || !reward || roundEnded)
		return;

	new id = get_loguser_index();
	if(!is_user_connected(id) || hosties[id][0] == -1) return;

	hosties[id][0]++;

	if(hosties[id][0] >= reward)
	{
		if(!equal(weapon[id],"hegrenade") && !equal(weapon[id],"knife") && level[id] < get_weapon_num())
		{
			// didn't level off of it
			if(!change_score(id,1)) show_required_kills(id);
		}

		hosties[id][0] = -1;
	}
 }

 // hostage is rescued
 public logevent_hostage_rescued()
 {
	new reward = get_pcvar_num(gg_host_rescue_reward);

	if(!get_pcvar_num(gg_enabled) || !reward || roundEnded)
		return;

	new id = get_loguser_index();
	if(!is_user_connected(id) || hosties[id][1] == -1) return;

	hosties[id][1]++;

	if(hosties[id][1] >= reward)
	{
		if(!equal(weapon[id],"hegrenade") && !equal(weapon[id],"knife") && level[id] < get_weapon_num())
			change_level(id,1);

		hosties[id][1] = -1;
	}
 }

 // hostage is killed
 public logevent_hostage_killed()
 {
	new penalty = get_pcvar_num(gg_host_kill_penalty);

	if(!get_pcvar_num(gg_enabled) || !penalty)
		return;

	new id = get_loguser_index();
	if(!is_user_connected(id)) return;

	new name[32];
	get_user_name(id,name,31);

	if(score[id] - penalty < 0)
		gungame_print(0,id,"%L",LANG_PLAYER_C,"HK_LEVEL_DOWN",name,(level[id] > 1) ? level[id]-1 : level[id]);
	else
		gungame_print(0,id,"%L",LANG_PLAYER_C,"HK_SCORE_DOWN",name,penalty);

	change_score(id,-penalty);
 }

 // someone joins a team
 public logevent_team_join()
 {
	new id = get_loguser_index();
	if(!is_user_connected(id) || (oldTeam[id] != 1 && oldTeam[id] != 2)) return;

	static arg2[10];
	read_logargv(2,arg2,9);

	// get the new team
	new newTeam;
	if(equal(arg2,"TERRORIST")) newTeam = 1;
	else if(equal(arg2,"CT")) newTeam = 2;

	// didn't switch teams, too bad for you (suicide)
	if(!newTeam || oldTeam[id] == newTeam)
		return;

	// check to see if the team change was beneficial
	if(get_pcvar_num(gg_allow_changeteam) == 2)
	{
		new teamCount[2], i;
		for(i=1;i<=maxPlayers;i++)
		{
			if(!is_user_connected(i))
				continue;

			switch(cs_get_user_team(i))
			{
				case CS_TEAM_T: teamCount[0]++;
				case CS_TEAM_CT: teamCount[1]++;
			}
		}

		if(teamCount[newTeam-1] <= teamCount[oldTeam[id]-1])
			remove_task(TASK_DELAYED_SUICIDE+id);
	}
	else remove_task(TASK_DELAYED_SUICIDE+id);
 }

 //
 // COMMAND HOOKS
 //

 // turning GunGame on or off
 public cmd_gungame(id,level,cid)
 {
	// no access, or GunGame ending anyway
	if(!cmd_access(id,level,cid,2) || won)
		return PLUGIN_HANDLED;

	// already working on toggling GunGame
	if(task_exists(TASK_TOGGLE_GUNGAME + TOGGLE_FORCE)
	|| task_exists(TASK_TOGGLE_GUNGAME + TOGGLE_DISABLE)
	|| task_exists(TASK_TOGGLE_GUNGAME + TOGGLE_ENABLE))
	{
		console_print(id,"[GunGame] GunGame is already being turned on or off");
		return PLUGIN_HANDLED;
	}

	new arg[32], oldStatus = get_pcvar_num(gg_enabled), newStatus;
	read_argv(1,arg,31);

	if(equali(arg,"on") || str_to_num(arg))
		newStatus = 1;

	// no change
	if((!oldStatus && !newStatus) || (oldStatus && newStatus))
	{
		console_print(id,"[GunGame] GunGame is already %s!",(newStatus) ? "on" : "off");
		return PLUGIN_HANDLED;
	}

	set_task(4.8,"toggle_gungame",TASK_TOGGLE_GUNGAME+newStatus);
	set_cvar_num("sv_restartround",5);
	if(!newStatus) set_pcvar_num(gg_enabled,0);

	console_print(id,"[GunGame] Turned GunGame %s",(newStatus) ? "on" : "off");

	return PLUGIN_HANDLED;
 }

 // voting for GunGame
 public cmd_gungame_vote(id,lvl,cid)
 {
	if(!cmd_access(id,lvl,cid,1))
		return PLUGIN_HANDLED;

	autovote_start();
	console_print(id,"[GunGame] Started a vote to play GunGame");

	return PLUGIN_HANDLED;
 }

 // setting players levels
 public cmd_gungame_level(id,lvl,cid)
 {
	if(!cmd_access(id,lvl,cid,3))
		return PLUGIN_HANDLED;

	new arg1[32], arg2[32], targets[32], name[32], tnum, i;
	read_argv(1,arg1,31);
	read_argv(2,arg2,31);

	// get player list
	if(equali(arg1,"*") || equali(arg1,"@ALL"))
	{
		get_players(targets,tnum);
		name = "ALL PLAYERS";
	}
	else if(equali(arg1,"@TERRORIST") || equali(arg1,"@CT"))
	{
		new players[32], team[32], pnum;
		get_players(players,pnum);

		for(i=0;i<pnum;i++)
		{
			get_user_team(players[i],team,31);
			if(equali(team,arg1[1])) targets[tnum++] = players[i];
		}

		formatex(name,31,"ALL %s",arg1[1]);
	}
	else
	{
		targets[tnum++] = cmd_target(id,arg1,2);
		if(!targets[0]) return PLUGIN_HANDLED;

		get_user_name(targets[0],name,31);
	}

	new intval = str_to_num(arg2);

	// relative
	if(arg2[0] == '+' || arg2[0] == '-')
		for(i=0;i<tnum;i++) change_level(targets[i],intval,0,1,1);

	// absolute
	else
		for(i=0;i<tnum;i++) change_level(targets[i],intval-level[targets[i]],0,1,1);

	console_print(id,"[GunGame] Changed %s's level to %s",name,arg2);

	return PLUGIN_HANDLED;
 }

 // block fullupdate
 public cmd_fullupdate(id)
 {
	return PLUGIN_HANDLED;
 }

 // hook say
 public cmd_say(id)
 {
	if(!get_pcvar_num(gg_enabled)) return PLUGIN_CONTINUE;

	static message[256];
	read_argv(1,message,255);

	// doesn't begin with !, ignore
	if(message[0] != '!') return PLUGIN_CONTINUE;

	if(equali(message,"!rules") || equali(message,"!help"))
	{
		new num = 1, max_lvl = get_pcvar_num(gg_max_lvl), turbo = get_pcvar_num(gg_turbo);

		console_print(id,"-----------------------------");
		console_print(id,"-----------------------------");
		console_print(id,"*** Avalanche's %L %s %L ***",id,"GUNGAME",GG_VERSION,id,"RULES");
		console_print(id,"%L",id,"RULES_CONSOLE_LINE1",num++);
		console_print(id,"%L",id,"RULES_CONSOLE_LINE2",num++);
		if(get_pcvar_num(gg_bomb_defuse_lvl)) console_print(id,"%L",id,"RULES_CONSOLE_LINE3",num++);
		console_print(id,"%L",id,"RULES_CONSOLE_LINE4",num++);
		if(get_pcvar_num(gg_ff_auto)) console_print(id,"%L",id,"RULES_CONSOLE_LINE5",num++);
		if(turbo || !max_lvl) console_print(id,"%L",id,"RULES_CONSOLE_LINE6A",num++);
		else if(max_lvl == 1) console_print(id,"%L",id,"RULES_CONSOLE_LINE6B",num++);
		else if(max_lvl > 1) console_print(id,"%L",id,"RULES_CONSOLE_LINE6C",num++,max_lvl);
		console_print(id,"%L",id,"RULES_CONSOLE_LINE7",num++);
		if(get_pcvar_num(gg_knife_pro)) console_print(id,"%L",id,"RULES_CONSOLE_LINE8",num++);
		if(turbo) console_print(id,"%L",id,"RULES_CONSOLE_LINE9",num++);
		if(get_pcvar_num(gg_dm) || get_cvar_num("csdm_active")) console_print(id,"%L",id,"RULES_CONSOLE_LINE10",num++);
		console_print(id,"****************************************************************");
		console_print(id,"%L",id,"RULES_CONSOLE_LINE11");
		console_print(id,"%L",id,"RULES_CONSOLE_LINE12");
		console_print(id,"%L",id,"RULES_CONSOLE_LINE13");
		console_print(id,"%L",id,"RULES_CONSOLE_LINE14");
		console_print(id,"%L",id,"RULES_CONSOLE_LINE15");
		console_print(id,"-----------------------------");
		console_print(id,"-----------------------------");

		len = formatex(menuText,511,"%L^n",id,"RULES_MESSAGE_LINE1");
		len += formatex(menuText[len],511-len,"\d----------\w^n");
		len += formatex(menuText[len],511-len,"%L^n",id,"RULES_MESSAGE_LINE2");
		len += formatex(menuText[len],511-len,"\d----------\w^n");
		len += formatex(menuText[len],511-len,"%L^n",id,"RULES_MESSAGE_LINE3");
		len += formatex(menuText[len],511-len,"\d----------\w^n%L",id,"PRESS_KEY_TO_CONTINUE");

		show_menu(id,1023,menuText);

		return PLUGIN_HANDLED;
	}
	else if(equali(message,"!weapons") || equali(message,"!guns"))
	{
		page[id] = 1;
		show_weapons_menu(id);

		return PLUGIN_HANDLED;
	}
	else if(equali(message,"!top10"))
	{
		get_pcvar_string(gg_stats_file,sfFile,63);

		// stats disabled
		if(!sfFile[0] || !get_pcvar_num(gg_stats_mode))
		{
			client_print(id,print_chat,"%L",id,"NO_WIN_LOGGING");
			return PLUGIN_HANDLED;
		}

		page[id] = 1;
		show_top10_menu(id);

		return PLUGIN_HANDLED;
	}
	else if(equali(message,"!score") || equali(message,"!scores"))
	{
		page[id] = 1;
		show_scores_menu(id);

		return PLUGIN_HANDLED;
	}
	else if(equali(message,"!level"))
	{
		show_level_menu(id);

		return PLUGIN_HANDLED;
	}
	else if(equali(message,"!restart") || equali(message,"!reset"))
	{
		if(level[id] <= 1)
		{
			client_print(id,print_chat,"%L",id,"STILL_LEVEL_ONE");
			return PLUGIN_HANDLED;
		}

		len = formatex(menuText,511,"%L^n^n",id,"RESET_QUERY");
		len += formatex(menuText[len],511-len,"1. %L^n",id,"YES");
		len += formatex(menuText[len],511-len,"0. %L",id,"CANCEL");
		show_menu(id,MENU_KEY_1|MENU_KEY_0,menuText,-1,"restart_menu");

		return PLUGIN_HANDLED;
	}

	return PLUGIN_CONTINUE;
 }

 // joining a team
 public cmd_joinclass(id)
 {
	if(!get_pcvar_num(gg_enabled))
		return PLUGIN_CONTINUE;

	// allow us to join in on deathmatch
	if(!get_pcvar_num(gg_dm))
	{
		remove_task(TASK_CHECK_DEATHMATCH+id);
		set_task(10.0,"check_deathmatch",TASK_CHECK_DEATHMATCH+id);
		return PLUGIN_CONTINUE;
	}

	if(roundEnded || (bombStatus[3] == BOMB_PLANTED && !get_pcvar_num(gg_dm_spawn_afterplant)))
		return PLUGIN_CONTINUE;

	set_task(2.0,"check_joinclass",id);
	return PLUGIN_CONTINUE;
 }

 // wait a bit after joinclass to see if we should jump in
 public check_joinclass(id)
 {
	if(!is_user_connected(id)) return;

	// already respawning
	if(task_exists(TASK_RESPAWN+id) || task_exists(TASK_CHECK_RESPAWN+id)
	|| task_exists(TASK_CHECK_RESPAWN2+id) || is_user_alive(id))
		return;

	// not on a valid team
	new CsTeams:team = cs_get_user_team(id);
	if(team == CS_TEAM_UNASSIGNED || team == CS_TEAM_SPECTATOR) return;

	set_task(0.1,"respawn",TASK_RESPAWN+id);
 }

 //
 // MENU FUNCTIONS
 //

 // toggle the status of gungame
 public toggle_gungame(taskid)
 {
	new status = taskid-TASK_TOGGLE_GUNGAME, i;

	// clear player tasks and values
	for(i=1;i<=32;i++)
	{
		remove_task(TASK_RESPAWN+i);
		remove_task(TASK_CHECK_RESPAWN+i);
		remove_task(TASK_CHECK_RESPAWN2+i);
		remove_task(TASK_CHECK_DEATHMATCH+i);
		remove_task(TASK_REMOVE_PROTECTION+i);
		clear_values(i);

	}

	// clear temp saves
	for(i=0;i<TEMP_SAVES;i++) clear_save(TASK_CLEAR_SAVE+i);

	if(status == TOGGLE_FORCE || status == TOGGLE_ENABLE)
	{
		new cfgFile[64];
		get_gg_config_file(cfgFile,63);

		// run the gungame config
		if(cfgFile[0] && file_exists(cfgFile))
		{
			new command[512], file, i;

			file = fopen(cfgFile,"rt");
			while(file && !feof(file))
			{
				fgets(file,command,511);
				trim(command);

				// stop at a comment
				for(i=0;i<strlen(command)-2;i++)
				{
					// only check config-style (;) comments as first character,
					// since they could be used ie in gg_map_setup to separate
					// commands. also check for coding-style (//) comments
					if((i == 0 && command[i] == ';') || (command[i] == '/' && command[i+1] == '/'))
					{
						copy(command,i,command);
						break;
					}
				}

				// don't override our setting from amx_gungame
				if(containi(command,"gg_enabled") != -1 && status == TOGGLE_ENABLE)
					continue;

				trim(command);
				if(command[0]) server_cmd(command);
			}
			if(file) fclose(file);
		}
	}

	// set to what we chose from amx_gungame
	if(status != TOGGLE_FORCE) set_pcvar_num(gg_enabled,status);

	// execute all of those cvars that we just set
	server_exec();

	// run appropiate cvars
	map_start_cvars();

	// reset some things
	if(!get_pcvar_num(gg_enabled))
	{
		// clear HUD message
		if(warmup > 0)
		{
			set_hudmessage(255,255,255,-1.0,0.4,0,6.0,1.0,0.1,0.2,CHANNEL_WARMUP);
			show_hudmessage(0," ");
		}

		warmup = -1;
		voted = 0;
		won = 0;

		remove_task(TASK_WARMUP_CHECK);
	}

	stats_get_top_players(TOP_PLAYERS,top10,80);

	// game_player_equip
	manage_equips();
 }

 // run cvars that should be run on map start
 public map_start_cvars()
 {
	new setup[512];

	// gungame is disabled, run endmap_setup
	if(!get_pcvar_num(gg_enabled))
	{
		get_pcvar_string(gg_endmap_setup,setup,511);
		if(setup[0]) server_cmd(setup);
	}
	else
	{
		// run map setup
		get_pcvar_string(gg_map_setup,setup,511);
		if(setup[0]) server_cmd(setup);

		// warmup is -13 after its finished, this stops multiple warmups for multiple map iterations
		new warmup_value = get_pcvar_num(gg_warmup_timer_setting);
		if(warmup_value > 0 && warmup != -13)
		{
			warmup = warmup_value;
			set_task(0.1,"warmup_check",TASK_WARMUP_CHECK);
		}

		// random weapon orders
		do_rOrder();
	}
 }

 // select a random weapon order
 do_rOrder()
 {
	new i, maxRandom, cvar[20];
	for(i=1;i<=MAX_WEAPON_ORDERS+1;i++) // +1 so we can detect final
	{
		formatex(cvar,19,"gg_weapon_order%i",i);
		get_cvar_string(cvar,weaponOrder,415);
		trim(weaponOrder);

		// found a blank one, stop here
		if(!weaponOrder[0])
		{
			maxRandom = i - 1;
			break;
		}
	}

	// we found some random ones
	if(maxRandom)
	{
		new randomOrder[30], lastOIstr[6], lastOI, orderAmt;
		get_localinfo("gg_rand_order",randomOrder,29);
		get_localinfo("gg_last_oi",lastOIstr,5);
		lastOI = str_to_num(lastOIstr);
		orderAmt = get_rOrder_amount(randomOrder);

		// no random order yet, or amount of random orders changed
		if(!randomOrder[0] || orderAmt != maxRandom)
		{
			shuffle_rOrder(randomOrder,29,maxRandom);
			lastOI = 0;
		}

		// reached the end, reshuffle while avoiding this one
		else if(get_rOrder_index_val(orderAmt,randomOrder) == get_rOrder_index_val(lastOI,randomOrder))
		{
			shuffle_rOrder(randomOrder,29,maxRandom,lastOI);
			lastOI = 0;
		}

		new choice = get_rOrder_index_val(lastOI+1,randomOrder);

		// get its weapon order
		formatex(cvar,19,"gg_weapon_order%i",choice);
		get_cvar_string(cvar,weaponOrder,415);

		// set as current
		set_cvar_string("gg_weapon_order",weaponOrder);

		// remember for next time
		num_to_str(lastOI+1,lastOIstr,5);
		set_localinfo("gg_last_oi",lastOIstr);
	}
 }

 // get the value of an order index in an order string
 get_rOrder_index_val(index,randomOrder[])
 {
	// only one listed
	if(str_count(randomOrder,',') < 1)
		return str_to_num(randomOrder);

	// find preceding comma
	new search = str_find_num(randomOrder,',',index-1);

	// go until succeeding comma
	new extract[6];
	copyc(extract,5,randomOrder[search+1],',');

	return str_to_num(extract);
 }

 // gets the amount of orders in an order string
 get_rOrder_amount(randomOrder[])
 {
	return str_count(randomOrder,',')+1;
 }

 // shuffle up our random order
 stock shuffle_rOrder(randomOrder[],len,maxRandom,avoid=-1)
 {
	randomOrder[0] = 0;

	// fill up array with order indexes
	new order[MAX_WEAPON_ORDERS], i;
	for(i=0;i<maxRandom;i++) order[i] = i+1;

	// shuffle it
	SortCustom1D(order,maxRandom,"sort_shuffle");

	// avoid a specific number as the starting number
	while(avoid > 0 && order[0] == avoid)
		SortCustom1D(order,maxRandom,"sort_shuffle");

	// get them into a string
	for(i=0;i<maxRandom;i++)
	{
		format(randomOrder,len,"%s%s%i",randomOrder,(i>0) ? "," : "",order[i]);
		set_localinfo("gg_rand_order",randomOrder);
	}
 }

 // shuffle an array
 public sort_shuffle(elem1,elem2)
 {
	return random_num(-1,1);
 }

 // handle the welcome menu
 public welcome_menu_handler(id,key)
 {
	// just save welcomed status and let menu close
	welcomed[id] = 1;
	return PLUGIN_HANDLED;
 }

 // this menu does nothing but display stuff
 public level_menu_handler(id,key)
 {
	return PLUGIN_HANDLED;
 }

 // handle the reset level menu
 public restart_menu_handler(id,key)
 {
	if(level[id] <= 1)
	{
		client_print(id,print_chat,"%L",id,"STILL_LEVEL_ONE");
		return PLUGIN_HANDLED;
	}

	// 1. Yes
	if(key == 0)
	{
		new name[32];
		get_user_name(id,name,31);

		change_level(id,-(level[id]-1)); // back to level 1
		gungame_print(0,id,"%L",LANG_PLAYER_C,"PLAYER_RESET",name);
	}

	return PLUGIN_HANDLED;
 }

 // show the level display
 show_level_menu(id)
 {
	new goal, tied, leaderNum, leaderList[128], name[32];
	new bestLevel, bestPlayer = get_leader(bestLevel);
	len = 0;

	new players[32], num, i;
	get_players(players,num);

	if(bestPlayer == id)
	{
		// check for ties first
		for(i=0;i<num;i++)
		{
			if(level[players[i]] == level[id] && players[i] != id)
			{
				tied = 1;
				break;
			}
		}
	}
	else if(level[bestPlayer] == level[id]) tied = 1;

	// check for multiple leaders
	for(i=0;i<num;i++)
	{
		if(level[players[i]] == bestLevel)
		{
			if(++leaderNum == 5)
			{
				len += formatex(leaderList[len],127-len,", ...");
				break;
			}

			if(leaderList[0]) len += formatex(leaderList[len],127-len,", ");
			get_user_name(players[i],name,31);
			len += formatex(leaderList[len],127-len,"%s",name);
		}
	}

	goal = get_level_goal(level[id]);

	new displayWeapon[16];
	if(level[id]) formatex(displayWeapon,15,"%s",weapon[id]);
	else formatex(displayWeapon,15,"%L",id,"NONE");

	len = formatex(menuText,511,"%L %i (%s)^n",id,"ON_LEVEL",level[id],displayWeapon);
	len += formatex(menuText[len],511-len,"%L^n",id,"LEVEL_MESSAGE_LINE1",score[id],goal);

	if(bestPlayer == id && !tied) len += formatex(menuText[len],511-len,"%L^n",id,"LEVEL_MESSAGE_LINE2A");
	else if(tied) len += formatex(menuText[len],511-len,"%L^n",id,"LEVEL_MESSAGE_LINE2B");
	else len += formatex(menuText[len],511-len,"%L^n",id,"LEVEL_MESSAGE_LINE2C",bestLevel-level[id]);
	len += formatex(menuText[len],511-len,"\d----------\w^n");

	new authid[24], wins, points;

	if(get_pcvar_num(gg_stats_ip)) get_user_ip(id,authid,23);
	else get_user_authid(id,authid,23);

	stats_get_data(authid,wins,points,dummy,1,dummy[0]);

	new stats_mode = get_pcvar_num(gg_stats_mode);

	if(stats_mode)
	{
		if(stats_mode == 1) len += formatex(menuText[len],511-len,"%L^n",id,"LEVEL_MESSAGE_LINE3A",wins);
		else len += formatex(menuText[len],511-len,"%L (%i %L)^n",id,"LEVEL_MESSAGE_LINE3B",points,wins,id,"WINS");

		len += formatex(menuText[len],511-len,"\d----------\w^n");
	}

	if(leaderNum > 1) len += formatex(menuText[len],511-len,"%L^n",id,"LEVEL_MESSAGE_LINE4A",leaderList);
	else len += formatex(menuText[len],511-len,"%L^n",id,"LEVEL_MESSAGE_LINE4B",leaderList);

	if(level[bestPlayer]) formatex(displayWeapon,15,"%s",weapon[bestPlayer]);
	else formatex(displayWeapon,15,"%L",id,"NONE");

	len += formatex(menuText[len],511-len,"%L^n",id,"LEVEL_MESSAGE_LINE5",bestLevel,displayWeapon);
	len += formatex(menuText[len],511-len,"\d----------\w^n");

	len += formatex(menuText[len],511-len,"%L",id,"PRESS_KEY_TO_CONTINUE");
	show_menu(id,1023,menuText,-1,"level_menu");
 }

 // show the top10 list menu
 show_top10_menu(id)
 {
	new totalPlayers = TOP_PLAYERS, playersPerPage = 5, stats_mode = get_pcvar_num(gg_stats_mode);
	new pageTotal = floatround(float(totalPlayers) / float(playersPerPage),floatround_ceil);

	if(page[id] < 1) page[id] = 1;
	if(page[id] > pageTotal) page[id] = pageTotal;

	len = formatex(menuText,511-len,"\y%L %L (%i/%i)\w^n",id,"GUNGAME",id,"TOP_10",page[id],pageTotal);
	//len += formatex(menuText[len],511-len,"\d-----------\w^n");

	static top10listing[81];
	new start = (playersPerPage * (page[id]-1)), i;

	for(i=start;i<start+playersPerPage;i++)
	{
		if(i > totalPlayers) break;

		// blank
		if(!top10[i][0])
		{
			len += formatex(menuText[len],511-len,"#%i \d%L\w^n",i+1,id,"NONE");
			continue;
		}

		// assign it to a new variable so that strtok
		// doesn't tear apart our constant top10 variable
		top10listing = top10[i];

		// get rid of authid
		strtok(top10listing,sfAuthid,1,top10listing,80,'^t');

		// isolate wins
		strtok(top10listing,sfWins,5,top10listing,80,'^t');

		// isolate name
		strtok(top10listing,sfName,31,top10listing,80,'^t');

		// break off timestamp and get points
		strtok(top10listing,sfTimestamp,1,sfPoints,7,'^t');

		if(stats_mode == 1)
			len += formatex(menuText[len],511-len,"#%i %s (%s %L)^n",i+1,sfName,sfWins,id,"WINS");
		else
			len += formatex(menuText[len],511-len,"#%i %s (%i %L, %s %L)^n",i+1,sfName,str_to_num(sfPoints),id,"POINTS",sfWins,id,"WINS");
	}

	len += formatex(menuText[len],511-len,"\d-----------\w^n");

	new keys = MENU_KEY_0;

	if(page[id] > 1)
	{
		len += formatex(menuText[len],511-len,"1. %L^n",id,"PREVIOUS");
		keys |= MENU_KEY_1;
	}
	if(page[id] < pageTotal)
	{
		len += formatex(menuText[len],511-len,"2. %L^n",id,"NEXT");
		keys |= MENU_KEY_2;
	}
	len += formatex(menuText[len],511-len,"0. %L",id,"CLOSE");

	show_menu(id,keys,menuText,-1,"top10_menu");
 }

 // someone pressed a key on the top10 list menu page
 public top10_menu_handler(id,key)
 {
	new totalPlayers = TOP_PLAYERS, playersPerPage = 5;
	new pageTotal = floatround(float(totalPlayers) / float(playersPerPage),floatround_ceil);

	if(page[id] < 1 || page[id] > pageTotal) return;

	// 1. Previous
	if(key == 0)
	{
		page[id]--;
		show_top10_menu(id);

		return;
	}

	// 2. Next
	else if(key == 1)
	{
		page[id]++;
		show_top10_menu(id);

		return;
	}

	// 0. Close
	// do nothing, menu closes automatically
 }

 // show the weapon list menu
 show_weapons_menu(id)
 {
	new totalWeapons = get_weapon_num(), wpnsPerPage = 10;
	new pageTotal = floatround(float(totalWeapons) / float(wpnsPerPage),floatround_ceil);

	if(page[id] < 1) page[id] = 1;
	if(page[id] > pageTotal) page[id] = pageTotal;

	len = formatex(menuText,511-len,"\y%L %L (%i/%i)\w^n",id,"GUNGAME",id,"WEAPONS",page[id],pageTotal);
	//len += formatex(menuText[len],511-len,"\d-----------\w^n");

	new start = (wpnsPerPage * (page[id]-1)) + 1, i, wName[24];

	// are there any custom kill requirements?
	get_pcvar_string(gg_weapon_order,weaponOrder,415);
	new customKills = (containi(weaponOrder,":") != -1);

	for(i=start;i<start+wpnsPerPage;i++)
	{
		if(i > totalWeapons) break;

		get_weapon_name_by_level(i,wName,23);

		if(customKills)
			len += formatex(menuText[len],511-len,"%L %i: %s (%i)^n",id,"LEVEL",i,wName,get_level_goal(i));
		else
			len += formatex(menuText[len],511-len,"%L %i: %s^n",id,"LEVEL",i,wName);
	}

	len += formatex(menuText[len],511-len,"\d-----------\w^n");

	new keys = MENU_KEY_0;

	if(page[id] > 1)
	{
		len += formatex(menuText[len],511-len,"1. %L^n",id,"PREVIOUS");
		keys |= MENU_KEY_1;
	}
	if(page[id] < pageTotal)
	{
		len += formatex(menuText[len],511-len,"2. %L^n",id,"NEXT");
		keys |= MENU_KEY_2;
	}
	len += formatex(menuText[len],511-len,"0. %L",id,"CLOSE");

	show_menu(id,keys,menuText,-1,"weapons_menu");
 }

 // someone pressed a key on the weapon list menu page
 public weapons_menu_handler(id,key)
 {
	new totalWeapons = get_weapon_num(), wpnsPerPage = 10;
	new pageTotal = floatround(float(totalWeapons) / float(wpnsPerPage),floatround_ceil);

	if(page[id] < 1 || page[id] > pageTotal) return;

	// 1. Previous
	if(key == 0)
	{
		page[id]--;
		show_weapons_menu(id);
		return;
	}

	// 2. Next
	else if(key == 1)
	{
		page[id]++;
		show_weapons_menu(id);
		return;
	}

	// 0. Close
	// do nothing, menu closes automatically
 }

 // show the score list menu
 show_scores_menu(id)
 {
	new totalPlayers = get_playersnum(), playersPerPage = 5, stats_mode = get_pcvar_num(gg_stats_mode);
	new pageTotal = floatround(float(totalPlayers) / float(playersPerPage),floatround_ceil);

	if(page[id] < 1) page[id] = 1;
	if(page[id] > pageTotal) page[id] = pageTotal;

	new players[32], num;
	get_players(players,num);

	// order by highest level first
	SortCustom1D(players,num,"score_custom_compare");

	len = formatex(menuText,511,"\y%L %L (%i/%i)\w^n",id,"GUNGAME",id,"SCORES",page[id],pageTotal);
	//len += formatex(menuText[len],511-len,"\d-----------\w^n");

	new start = (playersPerPage * (page[id]-1)), i, name[32], player, authid[24], wins, points;

	// check for stats
	get_pcvar_string(gg_stats_file,sfFile,63);

	new stats_ip = get_pcvar_num(gg_stats_ip);

	for(i=start;i<start+playersPerPage;i++)
	{
		if(i >= totalPlayers) break;

		player = players[i];
		get_user_name(player,name,31);

		new displayWeapon[16];
		if(level[player] && weapon[player][0]) formatex(displayWeapon,15,"%s",weapon[player]);
		else formatex(displayWeapon,15,"%L",id,"NONE");

		if(sfFile[0] && stats_mode)
		{
			if(stats_ip) get_user_ip(player,authid,23);
			else get_user_authid(player,authid,23);

			stats_get_data(authid,wins,points,dummy,1,dummy[0]);

			len += formatex(menuText[len],511-len,"#%i %s, %L %i (%s), %i %L^n",i+1,name,id,"LEVEL",level[player],displayWeapon,(stats_mode == 1) ? wins : points,id,(stats_mode == 1) ? "WINS" : "POINTS");
		}
		else len += formatex(menuText[len],511-len,"#%i %s, %L %i (%s)^n",i+1,name,id,"LEVEL",level[player],displayWeapon);
	}

	len += formatex(menuText[len],511-len,"\d-----------\w^n");

	new keys = MENU_KEY_0;

	if(page[id] > 1)
	{
		len += formatex(menuText[len],511-len,"1. %L^n",id,"PREVIOUS");
		keys |= MENU_KEY_1;
	}
	if(page[id] < pageTotal)
	{
		len += formatex(menuText[len],511-len,"2. %L^n",id,"NEXT");
		keys |= MENU_KEY_2;
	}
	len += formatex(menuText[len],511-len,"0. %L",id,"CLOSE");

	show_menu(id,keys,menuText,-1,"scores_menu");
 }

 // sort list of players with their level first
 public score_custom_compare(elem1,elem2)
 {
	// invalid players
	if(elem1 < 1 || elem1 > 32 || elem2 < 1 || elem2 > 32)
		return 0;

	// tied levels, compare scores
	if(level[elem1] == level[elem2])
	{
		if(score[elem1] > score[elem2]) return -1;
		else if(score[elem1] < score[elem2]) return 1;
		else return 0;
	}

	// compare levels
	else if(level[elem1] > level[elem2]) return -1;
	else if(level[elem1] < level[elem2]) return 1;

	return 0; // equal
 }

 // someone pressed a key on the score list menu page
 public scores_menu_handler(id,key)
 {
	new totalPlayers = get_playersnum(), playersPerPage = 5;
	new pageTotal = floatround(float(totalPlayers) / float(playersPerPage),floatround_ceil);

	if(page[id] < 1 || page[id] > pageTotal) return;

	// 1. Previous
	if(key == 0)
	{
		page[id]--;
		show_scores_menu(id);
		return;
	}

	// 2. Next
	else if(key == 1)
	{
		page[id]++;
		show_scores_menu(id);
		return;
	}

	// 0. Close
	// do nothing, menu closes automatically
 }

 //
 // MAIN FUNCTIONS
 //

 // refresh a player's hegrenade stock
 public refresh_nade(taskid)
 {
	new id = taskid-TASK_REFRESH_NADE;

	// player left, player died, or GunGame turned off
	if(!is_user_connected(id) || !is_user_alive(id) || !get_pcvar_num(gg_enabled)) return;

	// not on the grenade level, or have one already
	if(!equali(weapon[id],"hegrenade") || cs_get_user_bpammo(id,CSW_HEGRENADE))
		return;

	// give another hegrenade
	fm_give_item(id,"weapon_hegrenade");
 }

 // keep checking if a player needs to rejoin
 public check_deathmatch(taskid)
 {
	new id = taskid-TASK_CHECK_DEATHMATCH;

	// left the game, or gungame is now disabled
	if(!is_user_connected(id) || !get_pcvar_num(gg_enabled)) return;

	// now on spectator
	new CsTeams:team = cs_get_user_team(id);
	if(team == CS_TEAM_UNASSIGNED || team == CS_TEAM_SPECTATOR) return;

	// DM still not enabled, keep waiting
	if(!get_pcvar_num(gg_dm))
	{
		set_task(10.0,"check_deathmatch",taskid);
		return;
	}

	// DM is enabled, respawn
	if(!is_user_alive(id)) set_task(0.1,"respawn",TASK_RESPAWN+id);
 }

 // show someone a welcome message
 public show_welcome(id)
 {
	if(welcomed[id]) return;

	new menuid, keys;
	get_user_menu(id,menuid,keys);

	// another old-school menu opened
	if(menuid > 0)
	{
		// wait and try again
		set_task(3.0,"show_welcome",id);
		return;
	}

	play_sound_by_cvar(id,gg_sound_welcome);

	len = formatex(menuText,511,"\y%L\w^n",id,"WELCOME_MESSAGE_LINE1",GG_VERSION);
	len += formatex(menuText[len],511-len,"\d---------------\w^n");

	new special;
	if(get_pcvar_num(gg_knife_pro))
	{
		len += formatex(menuText[len],511-len,"%L^n",id,"WELCOME_MESSAGE_LINE2");
		special = 1;
	}
	if(get_pcvar_num(gg_turbo))
	{
		len += formatex(menuText[len],511-len,"%L^n",id,"WELCOME_MESSAGE_LINE3");
		special = 1;
	}
	if(get_pcvar_num(gg_dm) || get_cvar_num("csdm_active"))
	{
		len += formatex(menuText[len],511-len,"%L^n",id,"WELCOME_MESSAGE_LINE4");
		special = 1;
	}

	if(special) len += formatex(menuText[len],511-len,"\d---------------\w^n");
	len += formatex(menuText[len],511-len,"%L",id,"WELCOME_MESSAGE_LINE5");
	len += formatex(menuText[len],511-len,"\d---------------\w^n");
	len += formatex(menuText[len],511-len,"%L",id,"PRESS_KEY_TO_CONTINUE");

	show_menu(id,1023,menuText,-1,"welcome_menu");
 }

 // show the required kills message
 show_required_kills(id)
 {
	gungame_hudmessage(id,3.0,"%L, %i/%i",id,"REQUIRED_KILLS",score[id],get_level_goal(level[id]));
 }

 // player killed himself
 player_suicided(id)
 {
	// we still have protection (round ended, new one hasn't started yet)
	if(roundEnded) return;

	static name[32];
	get_user_name(id,name,31);

	gungame_print(0,id,"%L",LANG_PLAYER_C,"SUICIDE_LEVEL_DOWN",name);

	// decrease level if we can
	change_level(id,-1);
 }

 // player scored or lost a point
 public change_score(id,value)
 {
	if(!can_score(id)) return 0;

	new oldScore = score[id], goal = get_level_goal(level[id]);

	// if this is going to level us
	if(score[id] + value >= goal)
	{
		new max_lvl = get_pcvar_num(gg_max_lvl);

		// already reached max levels this round
		if(!get_pcvar_num(gg_turbo) && max_lvl > 0 && levelsThisRound[id] >= max_lvl)
		{
			// put it as high as we can without leveling
			score[id] = goal - 1;
		}
		else score[id] += value;
	}
	else score[id] += value;

	// check for level up
	if(score[id] >= goal)
	{
		score[id] = 0;
		change_level(id,1);

		return 1;
	}

	// check for level down
	if(score[id] < 0)
	{
		goal = get_level_goal(level[id] > 1 ? level[id]-1 : 1);

		score[id] = (oldScore + value) + goal; // carry over points
		if(score[id] < 0) score[id] = 0;

		//if(level[id] > 1)
		change_level(id,-1);

		return -1;
	}

	// refresh menus
	new menu;
	get_user_menu(id,menu,dummy[0]);
	if(menu == level_menu) show_level_menu(id);

	new sdisplay = get_pcvar_num(gg_status_display);
	if(sdisplay == STATUS_KILLSLEFT || sdisplay == STATUS_KILLSDONE)
		status_display(id);

	if(value < 0)
		show_required_kills(id);
	else
		client_cmd(id,"speak ^"buttons/bell1.wav^"");

	if(get_pcvar_num(gg_refill_on_kill)) refill_ammo(id);

	return 0;
 }

 // player gained or lost a level
 stock change_level(id,value,just_joined=0,show_message=1,always_score=0)
 {
	if(level[id] > 0 && !always_score && !can_score(id))
		return;

	// knife warmup, don't bother leveling up
	if(level[id] > 0 && warmup > 0 && get_pcvar_num(gg_knife_warmup)) return;

	// this will put us below level 1
	if(level[id] + value < 1)
		value = 1 - level[id]; // go down only to level 1

	// going up
	if(value > 0)
	{
		new max_lvl = get_pcvar_num(gg_max_lvl);

		// already reached max levels for this round
		if(!get_pcvar_num(gg_turbo) && max_lvl > 0 && levelsThisRound[id] >= max_lvl)
			return;
	}

	level[id] += value;
	levelsThisRound[id] += value;

	// level up!
	if(value > 0) play_sound_by_cvar(id,gg_sound_levelup);

	// level down :(
	else play_sound_by_cvar(id,gg_sound_leveldown);

	// grab my name
	static name[32];
	get_user_name(id,name,31);

	// win???
	if(level[id] >= get_weapon_num()+1)
	{
		// still warming up (wow, this guy is good)
		if(warmup > 0)
		{
			change_level(id,-value,just_joined);

			client_print(id,print_chat,"%L",id,"SLOW_DOWN");
			client_print(id,print_center,"%L",id,"SLOW_DOWN");

			return;
		}

		// bot, and not allowed to win
		if(is_user_bot(id) && get_pcvar_num(gg_ignore_bots) == 2 && !only_bots())
		{
			change_level(id,-value,just_joined);
			return;
		}

		// crown the winner
		if(!won) win(id);

		return;
	}

	silenced[id] = 0; // for going to Glock->USP, for example

	// set weapon based on it
	get_weapon_name_by_level(level[id],weapon[id],23);

	new players[32], num, i, menu;
	get_players(players,num);

	// refresh menus
	for(i=0;i<num;i++)
	{
		get_user_menu(players[i],menu,dummy[0]);

		if(menu == scores_menu) show_scores_menu(players[i]);
		else if(menu == level_menu) show_level_menu(players[i]);
	}

	// make sure we don't have more than required now
	new goal = get_level_goal(level[id]);
	if(score[id] >= goal) score[id] = goal-1; // 1 under

	// status display
	new sdisplay = get_pcvar_num(gg_status_display);

	if(sdisplay == STATUS_LEADERWPN) status_display(0); // to all
	else if(sdisplay) status_display(id); // only to me

	// give weapon right away?
	new turbo = get_pcvar_num(gg_turbo);
	if((turbo || just_joined) && is_user_alive(id)) set_task(0.1,"give_level_weapon_delay",id);

	if(show_message) gungame_hudmessage(id,3.0,"%L %i (%s)",id,"NOW_ON_LEVEL",level[id],weapon[id]);

	new vote_setting = get_pcvar_num(gg_vote_setting), map_iterations = get_pcvar_num(gg_map_iterations);

	// the level to start a map vote on
	if(!voted && warmup <= 0 && vote_setting > 0
	&& level[id] >= get_weapon_num() - (vote_setting - 1)
	&& mapIteration >= map_iterations && map_iterations > 0)
	{
		new mapCycleFile[64];
		get_gg_mapcycle_file(mapCycleFile,63);

		// start map vote?
		if(!mapCycleFile[0] || !file_exists(mapCycleFile))
		{
			voted = 1;

			// check for a custom vote
			new custom[256];
			get_pcvar_string(gg_vote_custom,custom,255);

			if(custom[0]) server_cmd(custom);
			else start_mapvote();
		}
	}

	// only calculate position if we didn't just join
	if(!just_joined)
	{
		new bestLevel, bestPlayer = get_leader(bestLevel);

		// I'M THE BEST!!!!!!!
		if(bestPlayer == id)
		{
			// check for ties first
			new players[32], num, i, tied;
			get_players(players,num);

			for(i=0;i<num;i++)
			{
				if(level[players[i]] == level[id] && players[i] != id)
				{
					tied = 1;
					break;
				}
			}

			if(tied && level[id] > 1) gungame_print(0,id,"%L",LANG_PLAYER_C,"TIED_LEADER",name);
			else if(level[id] > 1) gungame_print(0,id,"%L",LANG_PLAYER_C,"LEADING_ON_LEVEL",name,level[id],weapon[id]);
		}

		// maybe still??
		else if(level[bestPlayer] == level[id])
		{
			if(level[id] > 1) gungame_print(0,id,"%L",LANG_PLAYER_C,"TIED_LEADER",name);
		}

		// awww...
		else gungame_print(id,0,"%L",id,"LEVELS_BEHIND_LEADER",level[bestPlayer]-level[id]);

		// triple bonus!
		if(levelsThisRound[id] == 3 && get_pcvar_num(gg_triple_on) && !turbo)
		{
			star[id] = 1;

			new sound[64];
			get_pcvar_string(gg_sound_triple,sound,63);

			fm_set_user_maxspeed(id,fm_get_user_maxspeed(id)*1.5);
			if(sound[0]) emit_sound(id,CHAN_VOICE,sound,VOL_NORM,ATTN_NORM,0,PITCH_NORM);
			set_pev(id,pev_effects,pev(id,pev_effects) | EF_BRIGHTLIGHT);
			fm_set_rendering(id,kRenderFxGlowShell,255,255,100,kRenderNormal,1);
			fm_set_user_godmode(id,1);

			message_begin(MSG_BROADCAST,SVC_TEMPENTITY);
			write_byte(22); // TE_BEAMFOLLOW
			write_short(id); // entity
			write_short(trailSpr); // sprite
			write_byte(20); // life
			write_byte(10); // width
			write_byte(255); // r
			write_byte(255); // g
			write_byte(100); // b
			write_byte(100); // brightness
			message_end();

			gungame_print(0,id,"%L",LANG_PLAYER_C,"TRIPLE_LEVELED",name);
			set_task(10.0,"end_star",TASK_END_STAR+id);
		}
	}

	new ff_auto = get_pcvar_num(gg_ff_auto), ff = get_cvar_num("mp_friendlyfire");

	// turn on FF?
	if(ff_auto && !ff && equal(weapon[id],"hegrenade"))
	{
		set_cvar_num("mp_friendlyfire",1);
		gungame_print(0,0,"%L",LANG_PLAYER_C,"FRIENDLYFIRE_ON");

		client_cmd(0,"speak ^"gungame/brass_bell_C.wav^"");
	}

	// turn off FF?
	else if(ff_auto && ff)
	{
		new keepFF, i;

		for(i=1;i<=maxPlayers;i++)
		{
			if(equal(weapon[i],"hegrenade") || equal(weapon[i],"knife"))
			{
				keepFF = 1;
				break;
			}
		}

		// no one is on nade or knife level anymore
		if(!keepFF) set_cvar_num("mp_friendlyfire",0);
	}
 }

 // delay before giving weapon after leveling. without this, the game
 // crashes without error. note that you couldn't just set_task to
 // give_level_weapon, because it has multiple parameters, and so
 // set_task won't accept it.
 public give_level_weapon_delay(id)
 {
	if(is_user_connected(id)) give_level_weapon(id,0);
 }

 // get rid of a player's star
 public end_star(taskid)
 {
	new id = taskid - TASK_END_STAR;
	if(!star[id]) return;

	star[id] = 0;
	//gungame_print(id,0,"Your star has run out!");

	if(is_user_alive(id))
	{
		fm_set_user_maxspeed(id,fm_get_user_maxspeed(id)/1.5);
		emit_sound(id,CHAN_VOICE,"common/null.wav",VOL_NORM,ATTN_NORM,0,PITCH_NORM); // stop sound
		set_pev(id,pev_effects,pev(id,pev_effects) & ~EF_BRIGHTLIGHT);
		fm_set_rendering(id);
		fm_set_user_godmode(id,0);

		message_begin(MSG_BROADCAST,SVC_TEMPENTITY);
		write_byte(99); // TE_KILLBEAM
		write_short(id); // entity
		message_end();
	}
 }

 // give a player a weapon based on his level
 stock give_level_weapon(id,notify=1,verify=1)
 {
	if(!is_user_alive(id) || pev(id,pev_iuser1)) return;

	give_essentials(id);

	new oldWeapon = get_user_weapon(id,dummy[0],dummy[0]);

	static wpnName[24];
	new weapons[32], wpnid, alright, num, i;
	get_user_weapons(id,weapons,num);

	new hasMain, hasGlock, hasSmoke, hasFlash;

	new ammo = get_pcvar_num(gg_ammo_amount);
	new knife_warmup = get_pcvar_num(gg_knife_warmup);
	new pickup_others = get_pcvar_num(gg_pickup_others);
	new myCategory = get_weapon_category(_,weapon[id]);

	new nade_glock = get_pcvar_num(gg_nade_glock);
	new nade_smoke = get_pcvar_num(gg_nade_smoke);
	new nade_flash = get_pcvar_num(gg_nade_flash);

	// remove stuff first
	for(i=0;i<num;i++)
	{
		wpnid = weapons[i];
		if(!wpnid) continue;

		// ignore knife and C4
		if(wpnid == CSW_KNIFE || wpnid == CSW_C4)
			continue;

		alright = 0;

		// if we shouldn't be doing knives only,
		// check to see if we already have certain weapons
		if(warmup <= 0 || !knife_warmup)
		{
			// check for special grenade allowances
			if(equal(weapon[id],"hegrenade"))
			{
				if(wpnid == CSW_GLOCK18 && nade_glock)
				{
					alright = 1;
					hasGlock = 1;
				}
				else if(wpnid == CSW_SMOKEGRENADE && nade_smoke)
				{
					alright = 1;
					hasSmoke = 1;
				}
				else if(wpnid == CSW_FLASHBANG && nade_flash)
				{
					alright = 1;
					hasFlash = 1;
				}
			}

			// weapon id -> weapon name
			get_weaponname(wpnid,wpnName,23);
			replace(wpnName,23,"weapon_","");

			// this is our designated weapon
			if(equal(weapon[id],wpnName))
			{
				alright = 1;
				hasMain = 1;
			}
		}

		// was it alright?
		if(alright)
		{
			// reset ammo
			if(wpnid != CSW_HEGRENADE && wpnid != CSW_SMOKEGRENADE && wpnid != CSW_FLASHBANG)
			{
				if(equal(weapon[id],"hegrenade") && wpnid == CSW_GLOCK18)
					cs_set_user_bpammo(id,wpnid,50);

				else if(ammo > 0) cs_set_user_bpammo(id,wpnid,ammo);
				else cs_set_user_bpammo(id,wpnid,maxAmmo[wpnid]);
			}
			else cs_set_user_bpammo(id,wpnid,1); // grenades
		}

		// only remove this if we aren't allowed to have any other weapons,
		// or if it takes up the same category as the weapon that we need
		else if(!pickup_others || get_weapon_category(wpnid) == myCategory)
		{
			// remove grenades by clearing bpammo
			if(wpnid == CSW_HEGRENADE || wpnid == CSW_SMOKEGRENADE || wpnid == CSW_FLASHBANG)
			{
				user_has_weapon(id,wpnid,0); // remove form
				cs_set_user_bpammo(id,wpnid,0); // remove function
			}

			// remove regular weapons with this
			else fm_strip_user_gun(id,wpnid);
		}
	}

	// give CTs defuse kits on bomb maps
	if(bombMap && !get_pcvar_num(gg_block_objectives) && cs_get_user_team(id) == CS_TEAM_CT)
		cs_set_user_defuse(id,1);

	new armor = get_pcvar_num(gg_give_armor), helmet = get_pcvar_num(gg_give_helmet);

	// giving armor and helmets away like candy
	if(helmet) cs_set_user_armor(id,armor,CS_ARMOR_VESTHELM);
	else cs_set_user_armor(id,armor,CS_ARMOR_KEVLAR);

	// extras for grenade level
	if((warmup <= 0 || !knife_warmup) && equal(weapon[id],"hegrenade"))
	{
		if(nade_glock && !hasGlock)
		{
			fm_give_item(id,"weapon_glock18");
			cs_set_user_bpammo(id,CSW_GLOCK18,50);
		}
		if(nade_smoke && !hasSmoke) fm_give_item(id,"weapon_smokegrenade");
		if(nade_flash && !hasFlash) fm_give_item(id,"weapon_flashbang");
	}

	if(notify)
	{
		if(warmup > 0)
		{
			if(knife_warmup) gungame_print(id,-1,"%L -- %L",id,"WARMUP_ROUND",id,"KNIVES_ONLY");
			else gungame_print(id,-1,"%L",id,"WARMUP_ROUND");
		}
		else if(level[id] <= get_weapon_num()) // didn't just win
		{
			new goal = get_level_goal(level[id]);

			gungame_print(id,-1,"%L %%n%i%%e :: %%n%s%%e",id,"ON_LEVEL",level[id],weapon[id]);
			gungame_print(id,-1,"%L",id,"PROGRESS_DISPLAY",goal-score[id],score[id],goal);
		}

		//[GunGame] You are on level 9  : :  tmp
		//[GunGame] You need  2  kills to advance.  Score :  0 / 2
	}

	// don't give anything away if knife-only warmup
	if((warmup > 0 && knife_warmup) || equal(weapon[id],"knife"))
	{
		// draw knife on knife warmup and knife level... this is so that
		// the terrorist that spawns with the C4 won't be spawned with his
		// C4 selected, but instead his knfe
		client_cmd(id,"weapon_knife");
		return;
	}

	if(weapon[id][0] && !hasMain)
	{
		// stop double grenades
		if(equal(weapon[id],"hegrenade") && cs_get_user_bpammo(id,CSW_HEGRENADE))
			return;

		formatex(wpnName,23,"weapon_%s",weapon[id]);

		// make sure player gets his weapon
		for(i=0;i<3;i++)
		{
			if(fm_give_item(id,wpnName))
				break;
		}

		remove_task(TASK_REFRESH_NADE+id);

		if(!equal(weapon[id],"hegrenade"))
		{
			wpnid = get_weaponid(wpnName);

			if(!wpnid) log_amx("INVALID WEAPON ID FOR ^"%s^"",weapon[id]);
			else
			{
				if(ammo > 0) cs_set_user_bpammo(id,wpnid,ammo);
				else cs_set_user_bpammo(id,wpnid,maxAmmo[wpnid]);
			}
		}
	}

	// switch back to knife if we had it out. also don't do this when called
	// by the verification check, because their old weapon will obviously be
	// a knife and they will want to use their new one.
	if(verify && oldWeapon == CSW_KNIFE && !notify) client_cmd(id,"weapon_knife");

	// otherwise, switch to our new weapon
	else
	{
		// glock18 for nade level?
		if(equal(weapon[id],"hegrenade") && gg_nade_glock)
			wpnName = "weapon_glock18";
		else
			formatex(wpnName,23,"weapon_%s",weapon[id]);

		client_cmd(id,wpnName);
	}

	// remember burst or silenced status
	if(silenced[id])
	{
		if(equal(weapon[id],"usp") || equal(weapon[id],"m4a1"))
		{
			new wEnt = get_weapon_ent(id,_,weapon[id]);
			if(pev_valid(wEnt))
			{
				cs_set_weapon_silen(wEnt,1,0);

				// play draw with silencer animation
				if(weapon[id][0] == 'u') set_pev(id,pev_weaponanim,USP_DRAWANIM);
				else set_pev(id,pev_weaponanim,M4A1_DRAWANIM);
			}
		}
		else if(equal(weapon[id],"glock18") || equal(weapon[id],"famas"))
		{
			new wEnt = get_weapon_ent(id,_,weapon[id]);
			if(pev_valid(wEnt)) cs_set_weapon_burst(wEnt,1);
		}

		silenced[id] = 0;
	}

	// make sure that we get this...
	if(verify)
	{
		remove_task(TASK_VERIFY_WEAPON+id);
		set_task(1.0,"verify_weapon",TASK_VERIFY_WEAPON+id);
	}
 }

 // verify that we have our stupid weapon
 public verify_weapon(taskid)
 {
	new id = taskid-TASK_VERIFY_WEAPON;

	if(!is_user_alive(id)) return;

	static wpnName[24];
	formatex(wpnName,23,"weapon_%s",weapon[id]);
	new wpnid = get_weaponid(wpnName);

	if(!wpnid) return;

	// we don't have it, but we want it
	if((wpnid != CSW_HEGRENADE && !user_has_weapon(id,wpnid))
	|| (wpnid == CSW_HEGRENADE && !cs_get_user_bpammo(id,CSW_HEGRENADE)))
		give_level_weapon(id,0,0);
 }

 // bring someone back to life
 public begin_respawn(id)
 {
	if(!get_pcvar_num(gg_enabled) || !get_pcvar_num(gg_dm) || !is_user_connected(id))
		return;

	// now on spectator
	new CsTeams:team = cs_get_user_team(id);
	if(team == CS_TEAM_UNASSIGNED || team == CS_TEAM_SPECTATOR) return;

	// alive, and not in the broken sort of way
	if(is_user_alive(id) && !pev(id,pev_iuser1)) return;

	// round is over, or bomb is planted
	if(roundEnded || (bombStatus[3] == BOMB_PLANTED && !get_pcvar_num(gg_dm_spawn_afterplant)))
		return;

	new Float:delay = get_pcvar_float(gg_dm_spawn_delay);
	if(delay < 0.1) delay = 0.1;

	new dm_countdown = get_pcvar_num(gg_dm_countdown);

	if((dm_countdown & 1) || (dm_countdown & 2))
	{
		respawn_timeleft[id] = floatround(delay);
		respawn_countdown(id);
	}

	remove_task(TASK_RESPAWN+id);
	set_task(delay,"respawn",TASK_RESPAWN+id);
 }

 // show the respawn countdown to a player
 public respawn_countdown(id)
 {
	if(!is_user_connected(id) || is_user_alive(id))
	{
		respawn_timeleft[id] = 0;
		return;
	}

	new dm_countdown = get_pcvar_num(gg_dm_countdown);

	if(dm_countdown & 1)
		client_print(id,print_center,"%L",id,"RESPAWN_COUNTDOWN",respawn_timeleft[id]);
	
	if(dm_countdown & 2)
		gungame_hudmessage(id,1.0,"%L",id,"RESPAWN_COUNTDOWN",respawn_timeleft[id]);

	if(--respawn_timeleft[id] >= 1) set_task(1.0,"respawn_countdown",id);
 }

 // REALLY bring someone back to life
 public respawn(taskid)
 {
	new id = taskid-TASK_RESPAWN;
	if(!is_user_connected(id) || !get_pcvar_num(gg_enabled)) return;

	// round is over, or bomb is planted
	if(roundEnded || (bombStatus[3] == BOMB_PLANTED && !get_pcvar_num(gg_dm_spawn_afterplant)))
		return;

	// now on spectator
	new CsTeams:team = cs_get_user_team(id);
	if(team == CS_TEAM_UNASSIGNED || team == CS_TEAM_SPECTATOR) return;

	// clear countdown
	new dm_countdown = get_pcvar_num(gg_dm_countdown);
	if(dm_countdown & 1) client_print(id,print_center," ");
	if(dm_countdown & 2) gungame_hudmessage(id,1.0," ");

	// alive, and not in the broken sort of way
	if(is_user_alive(id) && !pev(id,pev_iuser1)) return;

	// remove his dropped weapons from before
	new ent;
	static model[22];
	while((ent = fm_find_ent_by_class(ent,"weaponbox")) != 0)
	{
		pev(ent,pev_model,model,21);

		// don't remove the bomb!! (thanks ToT | V!PER)
		if(equal(model,"models/w_c4.mdl",15) || equal(model,"models/w_backpack.mdl"))
			continue;

		// this is mine
		if(pev(ent,pev_owner) == id)
			dllfunc(DLLFunc_Think,ent);
	}

	new spawn_random = get_pcvar_num(gg_dm_spawn_random);
	if(spawn_random) spawnSounds[id] = 0;

	if(csrespawnEnabled)
		cs_respawn(id);
	else
	{
		// properly respawn
		blockCorpse[id] = 1;
		fm_cs_user_spawn(id);

		// make sure they made it
		remove_task(TASK_CHECK_RESPAWN+id);
		remove_task(TASK_CHECK_RESPAWN2+id);
		set_task(0.5,"check_respawn",TASK_CHECK_RESPAWN+id);
		set_task(1.5,"check_respawn",TASK_CHECK_RESPAWN2+id);
	}

	if(spawn_random)
	{
		do_random_spawn(id,spawn_random);
		spawnSounds[id] = 1;

		// to be fair, play a spawn noise at new location
		engfunc(EngFunc_EmitSound,id,CHAN_ITEM,"items/gunpickup2.wav",VOL_NORM,ATTN_NORM,0,PITCH_NORM);
	}

	new Float:time = get_pcvar_float(gg_dm_sp_time);
	new mode = get_pcvar_num(gg_dm_sp_mode);

	// spawn protection
	if(time > 0.0 && mode)
	{
		spawnProtected[id] = 1;
		if(mode == 2)
		{
			fm_set_user_godmode(id,1);
			fm_set_rendering(id,kRenderFxGlowShell,200,200,100,kRenderNormal,1); // goldenish
		}
		else fm_set_rendering(id,kRenderFxGlowShell,100,100,100,kRenderNormal,1); // gray/white

		set_task(time,"remove_spawn_protection",TASK_REMOVE_PROTECTION+id);
	}
 }

 // place a user at a random spawn
 do_random_spawn(id,spawn_random)
 {
	// not even alive, don't brother
	if(!is_user_alive(id) || pev(id,pev_iuser1))
		return;

	// no spawns???
	if(spawnCount <= 0) return;

	// no CSDM spawns, mode 2
	if(spawn_random == 2 && !csdmSpawnCount)
		return;

	static Float:vecHolder[3];
	new sp_index = random_num(0,spawnCount-1);

	// get origin for comparisons
	vecHolder[0] = spawns[sp_index][0];
	vecHolder[1] = spawns[sp_index][1];
	vecHolder[2] = spawns[sp_index][2];

	// this one is taken
	if(!is_hull_vacant(vecHolder,HULL_HUMAN) && spawnCount > 1)
	{
		new i;
		for(i=sp_index+1;i!=sp_index;i++)
		{
			// start over when we reach the end
			if(i >= spawnCount) i = 0;

			vecHolder[0] = spawns[i][0];
			vecHolder[1] = spawns[i][1];
			vecHolder[2] = spawns[i][2];

			// free space! office space!
			if(is_hull_vacant(vecHolder,HULL_HUMAN))
			{
				sp_index = i;
				break;
			}
		}
	}

	// origin
	vecHolder[0] = spawns[sp_index][0];
	vecHolder[1] = spawns[sp_index][1];
	vecHolder[2] = spawns[sp_index][2];
	engfunc(EngFunc_SetOrigin,id,vecHolder);

	// angles
	vecHolder[0] = spawns[sp_index][3];
	vecHolder[1] = spawns[sp_index][4];
	vecHolder[2] = spawns[sp_index][5];
	set_pev(id,pev_angles,vecHolder);

	// vangles
	vecHolder[0] = spawns[sp_index][6];
	vecHolder[1] = spawns[sp_index][7];
	vecHolder[2] = spawns[sp_index][8];
	set_pev(id,pev_v_angle,vecHolder);

	set_pev(id,pev_fixangle,1);
 }

 // make sure a user respawned alright
 public check_respawn(taskid)
 {
	new id;
	if(taskid > TASK_CHECK_RESPAWN2) id = taskid-TASK_CHECK_RESPAWN2;
	else id = taskid-TASK_CHECK_RESPAWN;

	blockCorpse[id] = 0;

	if(!is_user_connected(id))
		return;

	// now on spectator
	new CsTeams:team = cs_get_user_team(id);
	if(team == CS_TEAM_UNASSIGNED || team == CS_TEAM_SPECTATOR) return;

	// didn't respawn properly
	if(pev(id,pev_iuser1) || !is_user_alive(id))
	{
		//blockResetHUD[id]++; // no longer needed, since post_resethud checks pev_iuser1 now
		remove_task(TASK_CHECK_RESPAWN+id);
		remove_task(TASK_CHECK_RESPAWN2+id);
		respawn(TASK_RESPAWN+id); // respawn them again
	}
	else
	{
		// double guarantee
		give_essentials(id);
		set_task(0.1,"give_essentials",id);
	}
 }

 // give a user a knife and HUD after
 // spawning in case he doesn't get one regularly
 public give_essentials(id)
 {
	if(!is_user_alive(id) || pev(id,pev_iuser1)) return;

	fm_set_user_suit(id,true,false);
	if(!user_has_weapon(id,CSW_KNIFE)) fm_give_item(id,"weapon_knife");
 }

 // get rid of the spawn protection effects
 public remove_spawn_protection(taskid)
 {
	new id = taskid-TASK_REMOVE_PROTECTION;

	if(!is_user_connected(id)) return;

	spawnProtected[id] = 0;
	if(get_pcvar_num(gg_dm_sp_mode) == 2) fm_set_user_godmode(id,0);
	
	fm_set_rendering(id); // reset back to normal
 }

 // crown a winner
 win(id)
 {
	if(won) return;

	won = 1;
	roundEnded = 1;

	set_cvar_num("sv_alltalk",1);
	play_sound_by_cvar(0,gg_sound_winner);

	new map_iterations = get_pcvar_num(gg_map_iterations);

	// final playthrough, get ready for next map
	if(mapIteration >= map_iterations && map_iterations > 0)
	{
		set_nextmap();
		set_task(10.0,"goto_nextmap");

		// as of 1.16, we always send a non-emessage intermission, because
		// other map changing plugins (as well as StatsMe) intercepting it
		// was causing problems.

		message_begin(MSG_ALL,SVC_INTERMISSION);
		message_end();
	}

	// get ready to go again!!
	else
	{
		client_cmd(0,"+showscores");

		set_task(4.9,"restart_gungame",czero ? get_cvar_num("bot_stop") : 0);
		set_task(10.0,"stop_win_sound");

		set_cvar_num("sv_restartround",5);
		if(czero) set_cvar_num("bot_stop",1); // freeze CZ bots
	}

	new players[32], num, i;
	get_players(players,num);

	// freeze and godmode everyone
	for(i=0;i<num;i++)
	{
		set_pev(players[i],pev_flags,pev(players[i],pev_flags) | FL_FROZEN);
		fm_set_user_godmode(players[i],1);
	}

	// we have an invalid winner here
	if(!is_user_connected(id) || !can_score(id))
		return;

	new winnerName[32], myName[32], authid[24], team[10];
	get_user_name(id,winnerName,31);

	for(i=0;i<5;i++) gungame_print(0,id,"%%n%s%%e %L!",winnerName,LANG_PLAYER_C,"WON");
	set_cvar_num("sv_alltalk",1);

	get_pcvar_string(gg_stats_file,sfFile,63);

	new stats_mode = get_pcvar_num(gg_stats_mode), stats_ip = get_pcvar_num(gg_stats_ip),
	ignore_bots = get_pcvar_num(gg_ignore_bots);

	// points system
	if(sfFile[0] && stats_mode == 2)
	{
		new wins, Float:flPoints, iPoints, totalPoints, player;

		for(i=0;i<num;i++)
		{
			player = players[i];

			// calculate points and add
			flPoints = float(level[player]) - 1.0;
			wins = 0;

			// winner gets bonus points plus a win
			if(player == id)
			{
				flPoints *= get_pcvar_float(gg_stats_winbonus);
				wins = 1;
			}

			// unnecessary
			if(flPoints <= 0.0 && !wins) continue;

			iPoints = floatround(flPoints);

			// it's okay to add to stats
			if(!ignore_bots || !is_user_bot(player))
				stats_add_to_score(player,wins,iPoints,dummy[0],totalPoints);

			// log it
			get_user_name(player,myName,31);

			if(stats_ip) get_user_ip(player,authid,23);
			else get_user_authid(player,authid,23);

			get_user_team(player,team,9);
			log_message("^"%s<%i><%s><%s>^" triggered ^"GunGame_Points^" amount ^"%i^"",myName,get_user_userid(player),authid,team,iPoints);

			// display it
			gungame_print(player,0,"%L",player,"GAINED_POINTS",iPoints,totalPoints);
		}
	}

	// regular wins, no points
	else if(sfFile[0] && stats_mode && (!ignore_bots || !is_user_bot(id)))
		stats_add_to_score(id,1,0,dummy[0],dummy[0]);

	if(stats_ip) get_user_ip(id,authid,23);
	else get_user_authid(id,authid,23);

	get_user_team(id,team,9);

	log_message("^"%s<%i><%s><%s>^" triggered ^"Won_GunGame^"",winnerName,get_user_userid(id),authid,team);
	//"Avalanche<2><VALVE_ID_LOOPBACK><TERRORIST>" triggered "Killed_A_Hostage"
 }

 // restart gungame, for the next map iteration
 public restart_gungame(old_bot_stop_value)
 {
	won = 0;
	mapIteration++;

	toggle_gungame(TASK_TOGGLE_GUNGAME + TOGGLE_ENABLE); // reset stuff
	do_rOrder(); // pick the next weapon order

	// remove cheesey scoreboard
	client_cmd(0,"-showscores");

	new players[32], num, i;
	get_players(players,num);

	// unfreeze and ungodmode everyone
	for(i=0;i<num;i++)
	{
		set_pev(players[i],pev_flags,pev(players[i],pev_flags) & ~FL_FROZEN);
		fm_set_user_godmode(players[i],0);
		welcomed[players[i]] = 1; // also don't show welcome again
	}
	if(czero) set_cvar_num("bot_stop",old_bot_stop_value); // unfreeze CZ bots

	// only have warmup once?
	if(!get_pcvar_num(gg_warmup_multi)) warmup = -13; // -13 is the magical stop number
	else warmup = -1; // -1 isn't magical at all... :(
 }

 // stop the winner sound (for multiple map iterations)
 public stop_win_sound()
 {
	new winSound[64];
	get_pcvar_string(gg_sound_winner,winSound,63);

	// stop winning sound
	if(containi(winSound,".mp3") != -1) client_cmd(0,"mp3 stop");
	else client_cmd(0,"speak null");
 }

 //
 // AUTOVOTE FUNCTIONS
 //

 // start the autovote
 public autovote_start()
 {
	// vote in progress
	if(autovotes[0] || autovotes[1]) return;

	new Float:autovote_time = get_pcvar_float(gg_autovote_time);

	format(menuText,511,"\y%L^n^n\w1. %L^n2. %L^n^n0. %L",LANG_PLAYER,"PLAY_GUNGAME",LANG_PLAYER,"YES",LANG_PLAYER,"NO",LANG_PLAYER,"CANCEL");

	show_menu(0,MENU_KEY_1|MENU_KEY_2|MENU_KEY_0,menuText,floatround(autovote_time),"autovote_menu");
	set_task(autovote_time,"autovote_result");
}

 // take in votes
 public autovote_menu_handler(id,key)
 {
	switch(key)
	{
		case 0: autovotes[1]++;
		case 1: autovotes[0]++;
		//case 9: let menu close
	}

	return PLUGIN_HANDLED;
 }

 // calculate end of vote
 public autovote_result()
 {
	new enable, enabled = get_pcvar_num(gg_enabled);

	if(autovotes[0] || autovotes[1])
	{
		if(float(autovotes[1]) / float(autovotes[0] + autovotes[1]) >= get_pcvar_float(gg_autovote_ratio))
			enable = 1;
	}

	gungame_print(0,-1,"%L (%L: %i, %L: %i)",LANG_PLAYER_C,(enable) ? "VOTING_SUCCESS" : "VOTING_FAILED",LANG_PLAYER_C,"YES",autovotes[1],LANG_PLAYER_C,"NO",autovotes[0]);

	if(enable && !enabled)
	{
		set_task(4.8,"toggle_gungame",TASK_TOGGLE_GUNGAME+TOGGLE_ENABLE);
		set_cvar_num("sv_restartround",5);
	}
	else if(!enable && enabled)
	{
		set_task(4.8,"toggle_gungame",TASK_TOGGLE_GUNGAME+TOGGLE_DISABLE);
		set_cvar_num("sv_restartround",5);
		set_pcvar_num(gg_enabled,0);
	}

	// reset votes
	autovotes[0] = 0;
	autovotes[1] = 0;
 }

 //
 // STAT FUNCTIONS
 //

 // add to a player's wins
 stats_add_to_score(id,wins,points,&newWins,&newPoints)
 {
	// stats disabled
	if(!get_pcvar_num(gg_stats_mode)) return 0;

	get_pcvar_string(gg_stats_file,sfFile,63);

	// stats disabled
	if(!sfFile[0]) return 0;

	// get data
	new authid[24], name[32];

	if(get_pcvar_num(gg_stats_ip)) get_user_ip(id,authid,23);
	else get_user_authid(id,authid,23);

	get_user_name(id,name,31);

	// clean up the name
	trim(name);
	replace_all(name,31,"^t"," ");

	// replace it with new data
	new oldWins, oldPoints, line;
	line = stats_get_data(authid,oldWins,oldPoints,dummy,1,dummy[0]);

	newWins = oldWins+wins;
	newPoints = oldPoints+points;

	return stats_set_data(authid,oldWins+wins,oldPoints+points,name,get_systime(),line);
 }

 // get a player's last used name and wins from save file
 stock stats_get_data(authid[],&wins,&points,lastName[],nameLen,&timestamp,knownLine=-1)
 {
	wins = 0;
	points = 0;
	timestamp = 0;

	// stats disabled
	if(!get_pcvar_num(gg_stats_mode)) return -1;

	get_pcvar_string(gg_stats_file,sfFile,63);

	// stats disabled/file doesn't exist
	if(!sfFile[0] || !file_exists(sfFile)) return -1;

	// storage format:
	// AUTHID	WINS	LAST_USED_NAME	TIMESTAMP	POINTS

	// reset
	sfLineData[0] = 0;

	// open 'er up, boys!
	new line, found, file = fopen(sfFile,"rt");
	if(!file) return -1;

	// go through it
	while(!feof(file))
	{
		fgets(file,sfLineData,80);
		line++;

		// go to the line we know
		if(knownLine > -1)
		{
			if(line-1 == knownLine)
			{
				found = 1;
				break;
			}
			else continue;
		}

		// isolate authid
		strtok(sfLineData,sfAuthid,23,dummy,1,'^t');

		// this is it, stop now because our
		// data is already stored in sfLineData
		if(equal(authid,sfAuthid))
		{
			found = 1;
			break;
		}
	}

	// close 'er up, boys! (hmm....)
	fclose(file);

	// couldn't find
	if(!found) return -1;

	// isolate authid
	strtok(sfLineData,sfAuthid,23,sfLineData,80,'^t');

	// isolate wins
	strtok(sfLineData,sfWins,5,sfLineData,80,'^t');
	wins = str_to_num(sfWins);

	// isolate name
	strtok(sfLineData,lastName,nameLen,sfLineData,80,'^t');

	// isolate timestamp
	strtok(sfLineData,sfTimestamp,11,sfPoints,7,'^t');
	timestamp = str_to_num(sfTimestamp);

	// isolate points (only thing left)
	points = str_to_num(sfPoints);

	// return the line we got it on
	if(knownLine > -1) return knownLine;

	return line - 1;
 }

 // set a player's last used name and wins from save file
 stock stats_set_data(authid[],wins,points,lastName[],timestamp,knownLine=-1)
 {
	// stats disabled
	if(!get_pcvar_num(gg_stats_mode)) return 0;

	get_pcvar_string(gg_stats_file,sfFile,63);

	// stats disabled
	if(!sfFile[0]) return 0;

	// storage format:
	// AUTHID	WINS	LAST_USED_NAME	TIMESTAMP	POINTS

	new tempFileName[65], sfFile_rename[64], newFile_rename[65], file;
	formatex(tempFileName,64,"%s2",sfFile); // our temp file, append 2

	// rename_file backwards compatibility (thanks Mordekay)
	formatex(sfFile_rename,63,"%s/%s",modName,sfFile);
	formatex(newFile_rename,64,"%s/%s",modName,tempFileName);

	// create stats file if it doesn't exist
	if(!file_exists(sfFile))
	{
		file = fopen(sfFile,"wt");
		fclose(file);
	}

	// copy over current stat file
	rename_file(sfFile_rename,newFile_rename);

	// rename failed?
	if(!file_exists(tempFileName)) return 0;

	new tempFile = fopen(tempFileName,"rt");
	new line, goal;
	file = fopen(sfFile,"wt");

	// go through our old copy and rewrite entries
	while(tempFile && file && !feof(tempFile))
	{
		fgets(tempFile,sfLineData,80);

		if(!sfLineData[0])
		{
			line++;
			continue;
		}

		// see if this is the line we are trying to overwrite
		if(!goal)
		{
			if(knownLine > -1)
			{
				if(line == knownLine) goal = 1;
			}
			else
			{
				// isolate authid
				strtok(sfLineData,sfAuthid,23,dummy,1,'^t');

				// this is what we are looking for
				if(equal(authid,sfAuthid)) goal = 1;
			}
		}

		// overwrite with new values
		if(goal == 1)
		{
			goal = -1;
			fprintf(file,"%s^t%i^t%s^t%i^t%i",authid,wins,lastName,timestamp,points);
			fputc(file,'^n');
		}

		// otherwise just copy it over as it was (newline is already included)
		else fprintf(file,"%s",sfLineData);

		line++;
	}

	// never found an existing entry, make a new one
	if(!goal)
	{
		fprintf(file,"%s^t%i^t%s^t%i^t%i",authid,wins,lastName,timestamp,points);
		fputc(file,'^n');
	}

	if(tempFile) fclose(tempFile);
	if(file) fclose(file);

	// remove our copy
	delete_file(tempFileName);

	return 1;
 }

 // update a user's timestamp
 stats_refresh_timestamp(authid[])
 {
	new wins, points, lastName[32], timestamp;
	new line = stats_get_data(authid,wins,points,lastName,31,timestamp);

	if(line > -1) stats_set_data(authid,wins,points,lastName,get_systime(),line);
 }

 // gets the top X amount of players into an array
 // of the format: storage[amount][storageLen]
 stats_get_top_players(amount,storage[][],storageLen)
 {
	// stats disabled
	if(!get_pcvar_num(gg_stats_mode)) return 0;

	get_pcvar_string(gg_stats_file,sfFile,63);

	// stats disabled/file doesn't exist
	if(!sfFile[0] || !file_exists(sfFile)) return 0;

	// storage format:
	// AUTHID	WINS	LAST_USED_NAME	TIMESTAMP	POINTS

	// not so much OMG OMG OMG OMG as of 1.16
	static tempList[TOP_PLAYERS+1][82], tempLineData[81];

	new count, stats_mode = get_pcvar_num(gg_stats_mode), score[10];

	// open sesame
	new file = fopen(sfFile,"rt");
	if(!file) return 0;

	// reading, reading, reading...
	while(!feof(file))
	{
		fgets(file,sfLineData,80);

		// empty line
		if(!sfLineData[0]) continue;

		// assign it to a new variable so that strtok
		// doesn't tear apart our constant sfLineData variable
		tempLineData = sfLineData;

		// get rid of authid
		strtok(tempLineData,dummy,1,tempLineData,80,'^t');

		// sort by wins
		if(stats_mode == 1)
		{
			strtok(tempLineData,score,9,tempLineData,1,'^t');
		}

		// sort by points
		else
		{
			// break off wins
			strtok(tempLineData,dummy,1,tempLineData,80,'^t');

			// break off name
			strtok(tempLineData,dummy,1,tempLineData,80,'^t');

			// break off timestamp and get points
			strtok(tempLineData,dummy,1,score,9,'^t');
		}

		// don't store more than 11
		if(count >= amount) count = amount;

		tempList[count][0] = str_to_num(score);
		formatex(tempList[count][1],81,"%s",sfLineData);
		count++;

		// filled list with 11, sort
		if(count > amount) SortCustom2D(tempList,count,"stats_custom_compare");
	}

	// nolisting
	if(!count)
	{
		fclose(file);
		return 0;
	}

	// not yet sorted (didn't reach 11 entries)
	else if(count <= amount)
		SortCustom2D(tempList,count,"stats_custom_compare");

	new i;

	// now that it's sorted, return it
	for(i=0;i<amount&&i<count;i++)
		formatex(storage[i],storageLen,"%s",tempList[i][1]);


	// close
	fclose(file);

	return 1;
 }

 // our custom sorting function (check first dimension for score)
 public stats_custom_compare(elem1[],elem2[])
 {
	if(elem1[0] > elem2[0]) return -1;
	else if(elem1[0] < elem2[0]) return 1;

	return 0;
 }

 // prune old entries
 stock stats_prune(max_time=-1)
 {
	get_pcvar_string(gg_stats_file,sfFile,63);

	// stats disabled/file doesn't exist
	if(!sfFile[0] || !file_exists(sfFile)) return 0;

	// -1 = use value from cvar
	if(max_time == -1) max_time = get_pcvar_num(gg_stats_prune);

	// 0 = no pruning
	if(max_time == 0) return 0;

	new tempFileName[65], sfFile_rename[64], newFile_rename[65];
	formatex(tempFileName,64,"%s2",sfFile); // our temp file, append 2

	// rename_file backwards compatibility (thanks Mordekay)
	formatex(sfFile_rename,63,"%s/%s",modName,sfFile);
	formatex(newFile_rename,64,"%s/%s",modName,tempFileName);

	// copy over current stat file
	rename_file(sfFile_rename,newFile_rename);

	// rename failed?
	if(!file_exists(tempFileName)) return 0;

	new tempFile = fopen(tempFileName,"rt");
	new file = fopen(sfFile,"wt");

	// go through our old copy and rewrite valid entries into the new copy
	new current_time = get_systime(), original[81], removed;
	while(tempFile && file && !feof(tempFile))
	{
		fgets(tempFile,sfLineData,80);

		if(!sfLineData[0]) continue;

		// save original
		original = sfLineData;

		// break off authid
		strtok(sfLineData,sfAuthid,1,sfLineData,80,'^t');

		// break off wins
		strtok(sfLineData,sfWins,1,sfLineData,80,'^t');

		// break off name, and thus get timestamp
		strtok(sfLineData,sfName,1,sfTimestamp,11,'^t');
		copyc(sfTimestamp,11,sfTimestamp,'^t'); // cut off points

		// not too old, write it to our new file
		if(current_time - str_to_num(sfTimestamp) <= max_time)
			fprintf(file,"%s",original); // newline is already included
		else
			removed++;
	}

	if(tempFile) fclose(tempFile);
	if(file) fclose(file);

	// remove our copy
	delete_file(tempFileName);
	return removed;
 }

 //
 // SUPPORT FUNCTIONS
 //

 stock get_level_goal(level)
 {
	get_pcvar_string(gg_weapon_order,weaponOrder,415);

	new comma = str_find_num(weaponOrder,',',level-1)+1;

	static crop[32];
	copyc(crop,31,weaponOrder[comma],',');

	new colon = containi(crop,":");

	// no custom goal
	if(colon == -1)
	{
		if(equal(crop,"hegrenade") || equal(crop,"knife"))
			return 1;
		else
			return get_pcvar_num(gg_kills_per_lvl);
	}

	static goal[4];
	copyc(goal,3,crop[colon+1],',');

	return str_to_num(goal);
 }

 // get the name of a weapon by level
 stock get_weapon_name_by_level(theLevel,var[],varLen,includeGoal=0)
 {
	if(!theLevel)
	{
		formatex(var,varLen,"glock18");
		return;
	}
	else if(theLevel > 32 || theLevel > get_weapon_num())
	{
		formatex(var,varLen,"knife");
		return;
	}

	static weapons[32][24];
	get_weapon_order(weapons);

	if(!includeGoal && containi(var,":")) // strip off goal if we don't want it
		copyc(var,varLen,weapons[theLevel-1],':');

	else
		formatex(var,varLen,"%s",weapons[theLevel-1]);

	strtolower(var);
 }

 // get the weapons, in order
 get_weapon_order(weapons[32][24])
 {
	get_pcvar_string(gg_weapon_order,weaponOrder,415);

	new i;
	for(i=0;i<32;i++)
	{
		// out of stuff
		if(strlen(weaponOrder) <= 1) break;

		// we still have a comma, go up to it
		if(containi(weaponOrder,",") != -1)
		{
			strtok(weaponOrder,weapons[i],23,weaponOrder,415,',');
			trim(weapons[i]);
		}

		// otherwise, finish up
		else
		{
			formatex(weapons[i],23,"%s",weaponOrder);
			trim(weapons[i]);
			break;
		}
	}
 }

 // get the number of weapons to go through
 get_weapon_num()
 {
	get_pcvar_string(gg_weapon_order,weaponOrder,415);
	return str_count(weaponOrder,',') + 1;
 }

 // easy function to precache sound via cvar
 precache_sound_by_cvar(cvar[],default_val[])
 {
	new value[64];
	get_cvar_string(cvar,value,63);

	// mp3s precache as generic (full filepath)
	if(containi(value,".mp3") != -1 || (!value[0] && containi(default_val,".mp3") != -1))
	{
		if(!strlen(value) || !file_exists(value)) precache_generic(default_val);
		else precache_generic(value);
	}

	// wavs precache as sound (in sound/ directory by default)
	else
	{
		new filepath[64];
		formatex(filepath,63,"sound/%s",value);

		if(!strlen(value) || !file_exists(filepath)) precache_sound(default_val);
		else precache_sound(value);
	}
 }

 // figure out which gungame.cfg file to use
 get_gg_config_file(filename[],len)
 {
	formatex(filename,len,"%s/gungame.cfg",cfgDir);

	if(!file_exists(filename))
	{
		formatex(filename,len,"gungame.cfg");
		if(!file_exists(filename)) filename[0] = 0;
	}
 }

 // figure out which gungame_mapcycle file to use
 get_gg_mapcycle_file(filename[],len)
 {
	static testFile[64];

	// cstrike/addons/amxmodx/configs/gungame_mapcycle.cfg
	formatex(testFile,63,"%s/gungame_mapcycle.cfg",cfgDir);
	if(file_exists(testFile))
	{
		formatex(filename,len,"%s",testFile);
		return 1;
	}

	// cstrike/addons/amxmodx/configs/gungame_mapcycle.txt
	formatex(testFile,63,"%s/gungame_mapcycle.txt",cfgDir);
	if(file_exists(testFile))
	{
		formatex(filename,len,"%s",testFile);
		return 1;
	}

	// cstrike/gungame_mapcycle.cfg
	testFile = "gungame_mapcycle.cfg";
	if(file_exists(testFile))
	{
		formatex(filename,len,"%s",testFile);
		return 1;
	}

	// cstrike/gungame_mapcycle.txt
	testFile = "gungame_mapcycle.txt";
	if(file_exists(testFile))
	{
		formatex(filename,len,"%s",testFile);
		return 1;
	}

	return 0;
 }

 // another easy function to play sound via cvar
 play_sound_by_cvar(id,cvar)
 {
	static value[64];
	get_pcvar_string(cvar,value,63);

	if(!value[0]) return;

	if(containi(value,".mp3") != -1) client_cmd(id,"mp3 play ^"%s^"",value);
	else client_cmd(id,"speak ^"%s^"",value);
 }

 // find the highest level player and his level
 get_leader(&Rlevel)
 {
	new players[32], num, i, leader;
	get_players(players,num);

	// locate highest player
	for(i=0;i<num;i++)
	{
		if(leader == 0 || level[players[i]] > level[leader])
			leader = players[i];
	}

	Rlevel = level[leader];
	return leader;
 }

 // a butchered version of teame06's CS Color Chat Function
 gungame_print(id,custom,msg[],{Float,Sql,Result,_}:...)
 {
	new changeCount, num, i, j;
	static newMsg[191], message[191], changed[5], players[32];

	if(id)
	{
		players[0] = id;
		num = 1;
	}
	else get_players(players,num);

	new colored_messages = get_pcvar_num(gg_colored_messages);

	for(i=0;i<num;i++)
	{
		changeCount = 0;

		// we have to change LANG_PLAYER into
		// a player-specific argument, because
		// ML doesn't work well with SayText
		for(j=3;j<numargs();j++)
		{
			if(getarg(j) == LANG_PLAYER_C)
			{
				setarg(j,0,players[i]);
				changed[changeCount++] = j;
			}
		}

		// do user formatting
		vformat(newMsg,190,msg,4);

		// and now we have to change what we changed
		// back into LANG_PLAYER, so that the next
		// player will be able to have it in his language
		for(j=0;j<changeCount;j++)
		{
			setarg(changed[j],0,LANG_PLAYER_C);
		}

		// optimized color swapping
		if(colored_messages)
		{
			replace_all(newMsg,190,"%n","^x03"); // %n = team color
			replace_all(newMsg,190,"%g","^x04"); // %g = green
			replace_all(newMsg,190,"%e","^x01"); // %e = regular
		}
		else
		{
			replace_all(newMsg,190,"%n","");
			replace_all(newMsg,190,"%g","");
			replace_all(newMsg,190,"%e","");
		}

		// now do our formatting (I used two variables because sharing one caused glitches)

		if(custom == -1) formatex(message,190,"^x04[%L]^x01 %s",players[i],"GUNGAME",newMsg);
		else formatex(message,190,"^x01%s",newMsg);

		message_begin(MSG_ONE,gmsgSayText,_,players[i]);
		write_byte((custom > 0) ? custom : players[i]);
		write_string(message);
		message_end();
	}
 }

 // show a HUD message to a user
 gungame_hudmessage(id,Float:holdTime,msg[],{Float,Sql,Result,_}:...)
 {
	// user formatting
	static newMsg[191];
	vformat(newMsg,190,msg,4);

	// show it
	set_hudmessage(255,255,255,-1.0,0.8,0,6.0,holdTime,0.1,0.5,CHANNEL_STATUS);
	show_hudmessage(id,newMsg);
 }

 // start a map vote
 start_mapvote()
 {
	new dmmName[32];

	// AMXX Nextmap Chooser
	if(find_plugin_byfile("mapchooser.amxx") != INVALID_PLUGIN_ID)
	{
		new oldWinLimit = get_cvar_num("mp_winlimit"), oldMaxRounds = get_cvar_num("mp_maxrounds");
		set_cvar_num("mp_winlimit",0); // skip winlimit check
		set_cvar_num("mp_maxrounds",-1); // trick plugin to think game is almost over

		// call the vote
		if(callfunc_begin("voteNextmap","mapchooser.amxx") == 1)
			callfunc_end();

		// set maxrounds back
		set_cvar_num("mp_winlimit",oldWinLimit);
		set_cvar_num("mp_maxrounds",oldMaxRounds);
	}

	// Deagles' Map Management 2.30b
	else if(find_plugin_byfile("deagsmapmanage230b.amxx") != INVALID_PLUGIN_ID)
	{
		dmmName = "deagsmapmanage230b.amxx";
	}

	// Deagles' Map Management 2.40
	else if(find_plugin_byfile("deagsmapmanager.amxx") != INVALID_PLUGIN_ID)
	{
		dmmName = "deagsmapmanager.amxx";
	}

	//  Mapchooser4
	else if(find_plugin_byfile("mapchooser4.amxx") != INVALID_PLUGIN_ID)
	{
		new oldWinLimit = get_cvar_num("mp_winlimit"), oldMaxRounds = get_cvar_num("mp_maxrounds");
		set_cvar_num("mp_winlimit",0); // skip winlimit check
		set_cvar_num("mp_maxrounds",1); // trick plugin to think game is almost over

		// deactivate g_buyingtime variable
		if(callfunc_begin("buyFinished","mapchooser4.amxx") == 1)
			callfunc_end();

		// call the vote
		if(callfunc_begin("voteNextmap","mapchooser4.amxx") == 1)
		{
			callfunc_push_str("",false);
			callfunc_end();
		}

		// set maxrounds back
		set_cvar_num("mp_winlimit",oldWinLimit);
		set_cvar_num("mp_maxrounds",oldMaxRounds);
	}

	// NOTHING?
	else log_amx("Using gg_vote_setting without mapchooser.amxx, mapchooser4.amxx, deagsmapmanage230b.amxx, or deagsmapmanager.amxx: could not start a vote!");

	// do DMM stuff
	if(dmmName[0])
	{
		// allow voting
		/*if(callfunc_begin("dmapvotemode",dmmName) == 1)
					{
			callfunc_push_int(0); // server
			callfunc_end();
		}*/

		new oldWinLimit = get_cvar_num("mp_winlimit"), Float:oldTimeLimit = get_cvar_float("mp_timelimit");
		set_cvar_num("mp_winlimit",99999); // don't allow extending
		set_cvar_float("mp_timelimit",0.0); // don't wait for buying
		set_cvar_num("enforce_timelimit",1); // don't change map after vote

		// call the vote
		if(callfunc_begin("startthevote",dmmName) == 1)
			callfunc_end();

		set_cvar_num("mp_winlimit",oldWinLimit);
		set_cvar_float("mp_timelimit",oldTimeLimit);

		// disallow further voting
		/*if(callfunc_begin("dmapcyclemode",dmmName) == 1)
		{
			callfunc_push_int(0); // server
			callfunc_end();
		}*/
	}
 }

 // set amx_nextmap to the next map
 set_nextmap()
 {
	new mapCycleFile[64];
	get_gg_mapcycle_file(mapCycleFile,63);

	// no mapcycle, leave amx_nextmap alone
	if(!mapCycleFile[0] || !file_exists(mapCycleFile))
	{
		set_localinfo("gg_cycle_num","0");
		return;
	}

	new strVal[10];

	// have not gotten cycleNum yet (only get it once, because
	// set_nextmap is generally called at least twice per map, and we
	// don't want to change it twice)
	if(cycleNum == -1)
	{
		get_localinfo("gg_cycle_num",strVal,9);
		cycleNum = str_to_num(strVal);
	}

	new firstMap[32], currentMap[32], lineData[32], i, line, foundMap;
	get_mapname(currentMap,31);

	new file = fopen(mapCycleFile,"rt");
	while(file && !feof(file))
	{
		fgets(file,lineData,31);

		trim(lineData);
		replace(lineData,31,".bsp",""); // remove extension

		// stop at a comment
		for(i=0;i<strlen(lineData)-2;i++)
		{
			// supports config-style (;) and coding-style (//)
			if(lineData[i] == ';' || (lineData[i] == '/' && lineData[i+1] == '/'))
			{
				copy(lineData,i,lineData);
				break;
			}
		}

		trim(lineData);
		if(!lineData[0]) continue;

		// save first map
		if(!firstMap[0]) formatex(firstMap,31,"%s",lineData);

		// we reached the line after our current map's line
		if(line == cycleNum+1)
		{
			// remember so
			foundMap = 1;

			// get ready to change to it
			set_cvar_string("amx_nextmap",lineData);

			// remember this map's line for next time
			num_to_str(line,strVal,9);
			set_localinfo("gg_cycle_num",strVal);

			break;
		}

		line++;
	}
	if(file) fclose(file);

	// we didn't find next map
	if(!foundMap)
	{
		// reset line number to first (it's zero-based)
		set_localinfo("gg_cycle_num","0");

		// no maps listed, go to current
		if(!firstMap[0]) set_cvar_string("amx_nextmap",currentMap);

		// go to first map listed
		else set_cvar_string("amx_nextmap",firstMap);
	}
 }

 // go to amx_nextmap
 public goto_nextmap()
 {
	set_nextmap(); // for good measure

	new mapCycleFile[64];
	get_gg_mapcycle_file(mapCycleFile,63);

	// no gungame mapcycle
	if(!mapCycleFile[0] || !file_exists(mapCycleFile))
	{
		new custom[256];
		get_pcvar_string(gg_changelevel_custom,custom,255);

		// try custom changelevel command
		if(custom[0])
		{
			server_cmd(custom);
			return;
		}
	}

	// otherwise, go to amx_nextmap
	new nextMap[32];
	get_cvar_string("amx_nextmap",nextMap,31);

	server_cmd("changelevel %s",nextMap);
 }

 // weapon display
 stock status_display(id,status=1)
 {
	new sdisplay;
	sdisplay = get_pcvar_num(gg_status_display);

	// display disabled
	if(!sdisplay) return;

	// dead
	if(id && (!is_user_alive(id) || pev(id,pev_iuser1)))
		return;

	new dest;
	static sprite[32];
	if(!id) dest = MSG_BROADCAST;
	else dest = MSG_ONE_UNRELIABLE;

	// disable display if status is 0, or we are doing a knife warmup
	if(!status || (warmup > 0 && get_pcvar_num(gg_knife_warmup)))
	{
		message_begin(dest,gmsgScenario,_,id);
		write_byte(0);
		message_end();

		return;
	}

	// weapon display
	if(sdisplay == STATUS_LEADERWPN || sdisplay == STATUS_YOURWPN)
	{
		if(sdisplay == STATUS_LEADERWPN)
		{
			new ldrLevel;
			get_leader(ldrLevel);

			// get leader's weapon
			if(ldrLevel <= 0) return;
			get_weapon_name_by_level(ldrLevel,sprite,31);
		}
		else
		{
			// get your weapon
			if(level[id] <= 0) return;
			formatex(sprite,31,"%s",weapon[id]);
		}

		strtolower(sprite);

		// sprite is d_grenade, not d_hegrenade
		if(sprite[0] == 'h') sprite = "grenade";

		// get true sprite name
		format(sprite,31,"d_%s",sprite);
	}

	// kill display
	else if(sdisplay == STATUS_KILLSLEFT)
	{
		new goal = get_level_goal(level[id]);
		formatex(sprite,31,"number_%i",goal-score[id]);
	}
	else if(sdisplay == STATUS_KILLSDONE)
	{
		formatex(sprite,31,"number_%i",score[id]);
	}

	// display it already!
	message_begin(dest,gmsgScenario,_,id);
	write_byte(1);
	write_string(sprite);
	write_byte(150);
	message_end();
 }

 // refill a player's ammo
 stock refill_ammo(id,current=0)
 {
	if(!is_user_alive(id) || pev(id,pev_iuser1)) return;

	new armor = get_pcvar_num(gg_give_armor), helmet = get_pcvar_num(gg_give_helmet);

	// giving armor and helmets away like candy
	if(helmet) cs_set_user_armor(id,armor,CS_ARMOR_VESTHELM);
	else cs_set_user_armor(id,armor,CS_ARMOR_KEVLAR);

	// no ammo for knives only
	if(warmup > 0 && get_pcvar_num(gg_knife_warmup)) return;

	// get weapon name and index
	new wpnid, curweapon = get_user_weapon(id,dummy[0],dummy[0]);

	if(current && (curweapon != CSW_KNIFE || equal(weapon[id],"knife")))
	{
		wpnid = curweapon;
	}
	else
	{
		static fullName[24];
		formatex(fullName,23,"weapon_%s",weapon[id]);
		wpnid = get_weaponid(fullName);
	}

	if(!wpnid) return;

	// no reason to refill a knife
	if(wpnid == CSW_KNIFE) return;

	new ammo, wEnt;
	ammo = get_pcvar_num(gg_ammo_amount);

	// don't give away hundreds of grenades
	if(wpnid != CSW_HEGRENADE)
	{
		// set clip ammo
		wEnt = get_weapon_ent(id,wpnid);
		if(pev_valid(wEnt)) cs_set_weapon_ammo(wEnt,maxClip[wpnid]);

		// set backpack ammo
		if(equal(weapon[id],"hegrenade") && wpnid == CSW_GLOCK18)
			cs_set_user_bpammo(id,wpnid,50);

		else if(ammo > 0) cs_set_user_bpammo(id,wpnid,ammo);
		else cs_set_user_bpammo(id,wpnid,maxAmmo[wpnid]);

		// update display if we need to
		if(curweapon == wpnid)
		{
			message_begin(MSG_ONE,gmsgCurWeapon,_,id);
			write_byte(1);
			write_byte(wpnid);
			write_byte(maxClip[wpnid]);
			message_end();
		}
	}

	// now do stupid grenade stuff
	else
	{
		if(get_pcvar_num(gg_nade_glock))
		{
			// set clip ammo
			wEnt = get_weapon_ent(id,CSW_GLOCK18);
			if(pev_valid(wEnt)) cs_set_weapon_ammo(wEnt,maxClip[CSW_GLOCK18]);

			// set backpack ammo
			cs_set_user_bpammo(id,CSW_GLOCK18,50);

			// update display if we need to
			if(curweapon == CSW_GLOCK18)
			{
				message_begin(MSG_ONE,gmsgCurWeapon,_,id);
				write_byte(1);
				write_byte(CSW_GLOCK18);
				write_byte(maxClip[CSW_GLOCK18]);
				message_end();
			}
		}

		if(get_pcvar_num(gg_nade_smoke) && !cs_get_user_bpammo(id,CSW_SMOKEGRENADE))
			fm_give_item(id,"weapon_smokegrenade");

		if(get_pcvar_num(gg_nade_flash) && !cs_get_user_bpammo(id,CSW_FLASHBANG))
			fm_give_item(id,"weapon_flashbang");

		if(!cs_get_user_bpammo(id,CSW_HEGRENADE))
		{
			fm_give_item(id,"weapon_hegrenade");
			remove_task(TASK_REFRESH_NADE+id);
		}
	}

	// keep knife out
	if(curweapon == CSW_KNIFE) client_cmd(id,"weapon_knife");
 }

 // find a player's weapon entity
 stock get_weapon_ent(id,wpnid=0,wpnName[]="")
 {
	// who knows what wpnName will be
	static newName[32];

	// need to find the name
	if(wpnid) get_weaponname(wpnid,newName,31);

	// go with what we were told
	else formatex(newName,31,"%s",wpnName);

	// prefix it if we need to
	if(!equal(newName,"weapon_",7))
		format(newName,31,"weapon_%s",newName);

	return fm_find_ent_by_owner(0,newName,id);
 }

 // counts number of chars in a string, by (probably) Twilight Suzuka
 stock str_count(str[],searchchar)
 {
	new i = 0;
	new maxlen = strlen(str);
	new count = 0;
	
	for(i=0;i<=maxlen;i++)
	{
		if(str[i] == searchchar)
			count++;
	}
	return count;
 }

 // find the nth occurance of a character in a string, based on str_count
 stock str_find_num(str[],searchchar,number)
 {
	new i;
	new maxlen = strlen(str);
	new found = 0;

	for(i=0;i<=maxlen;i++)
	{
		if(str[i] == searchchar)
		{
			if(++found == number)
				return i;
		}
	}
	return -1;
 }

 // gets a player id that triggered certain logevents, by VEN
 stock get_loguser_index()
 {
	static loguser[80], name[32];
	read_logargv(0,loguser,79);
	parse_loguser(loguser,name,31);

	return get_user_index(name);
 }

 // checks if a space is vacant, by VEN
 stock bool:is_hull_vacant(const Float:origin[3],hull)
 {
	new tr = 0;
	engfunc(EngFunc_TraceHull,origin,origin,0,hull,0,tr);

	if(!get_tr2(tr,TR_StartSolid) && !get_tr2(tr,TR_AllSolid) && get_tr2(tr,TR_InOpen))
		return true;
	
	return false;
 }

 // gets a weapon's category, for Counter-Strike
 stock get_weapon_category(id=0,name[]="")
 {
	if(name[0])
	{
		if(equal(name,"weapon_",7)) id = get_weaponid(name);
		else
		{
			static newName[32];
			formatex(newName,31,"weapon_%s",name);
			id = get_weaponid(newName);
		}
	}

 	return weaponSlots[id];
 }

 // if a player is allowed to score (at least 1 player on opposite team)
 can_score(id)
 {
	new CsTeams:team = cs_get_user_team(id);

	// find opposite team
	switch(team)
	{
		case CS_TEAM_T: team = CS_TEAM_CT;
		case CS_TEAM_CT: team = CS_TEAM_T;
		default: return 0; // spectator
	}

	new i;
	for(i=1;i<=maxPlayers;i++)
	{
		if(is_user_connected(i) && cs_get_user_team(i) == team)
			return 1;
	}

	// didn't find anyone on opposing team
	return 0;
 }

 // returns 1 if there are only bots in the server, 0 if not
 only_bots()
 {
	new i;
	for(i=1;i<=maxPlayers;i++)
	{
		if(is_user_connected(i) && !is_user_bot(i))
			return 0;
	}

	// didn't find any humans
	return 1;
 }
