package boardgames;
import java.awt.*;

import javax.swing.JLabel;

public class DiceLabel extends JLabel {

		public DiceLabel(String s){
			this.setBackground(new Color(0,120,0));
			this.setFont(new Font("Arial",Font.BOLD,22) );
			this.setText(s);
		}
		public DiceLabel(String s, Font f){
			this.setFont(f );
			this.setText(s);
		}
}
