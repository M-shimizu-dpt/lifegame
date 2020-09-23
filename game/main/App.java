/*
 * アプリの基本操作を行うクラス
 * 基本的な処理は全てこのクラスに実装
 */

package lifegame.game.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;

import lifegame.game.WaitThread;
import lifegame.game.map.print.Window;
import lifegame.game.object.Card;
import lifegame.game.object.Dice;
import lifegame.game.object.Player;
import lifegame.game.search.Searcher;

public class App  implements ActionListener{
	private Boolean startFlag = false;
	private Dice dice = new Dice();//サイコロ処理

    public static void main(String[] args) {
        App app = new App();
    	app.run();
    }

    private void run() {
    	Map<Integer,Player> players = new HashMap<Integer,Player>();//プレイヤー情報

    	JFrame initFrame = new JFrame("桃大郎電鉄");
    	initFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//アプリ終了
    	initFrame.setSize(800,600);
    	initFrame.setLayout(null);
    	initFrame.setLocationRelativeTo(null);
    	JLayeredPane init = initFrame.getLayeredPane();
    	JLabel labelTitle = new JLabel("桃大郎電鉄");
    	labelTitle.setFont(new Font("SansSerif", Font.ITALIC, 50));
    	labelTitle.setBounds(280, 10, 400, 60);
    	JLabel labelYear = new JLabel("何年プレイしますか？");
    	labelYear.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelYear.setBounds(20, 50, 200, 50);
    	JTextField textYear = new JTextField("3");
    	textYear.setBounds(20, 100, 200, 50);
    	JLabel labelPlayers = new JLabel("プレイヤーの人数は何人ですか？");
    	labelPlayers.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	labelPlayers.setBounds(20, 150, 300, 50);
    	JTextField textPlayers = new JTextField("1");
    	textPlayers.setBounds(20, 200, 200, 50);
    	JButton startButton = new JButton("始める");
    	startButton.setFont(new Font("SansSerif", Font.ITALIC, 20));
    	startButton.setBounds(600,490,180,60);
    	startButton.addActionListener(this);
    	init.add(labelTitle);
    	init.add(labelYear);
    	init.add(textYear);
    	init.add(labelPlayers);
    	init.add(textPlayers);
    	init.add(startButton);

    	initFrame.setVisible(true);
    	while(!startFlag) {
    		try {
    			Thread.sleep(100);
    		}catch(InterruptedException e) {

    		}
    	}
    	initFrame.setVisible(false);

    	assert(Integer.parseInt(textPlayers.getText())>=0 && Integer.parseInt(textPlayers.getText())<=3);
    	assert(Integer.parseInt(textYear.getText())>0 && Integer.parseInt(textYear.getText())<=100);

    	Window window = new Window();
    	initWindow(Integer.parseInt(textYear.getText()),Integer.parseInt(textPlayers.getText()));
    	Card.init(window);
    	for(int i=0;i<4;i++) {
  			if(Integer.parseInt(textPlayers.getText())>i) {//プレイヤー
	  			players.put(i,new Player("player"+(i+1),1000,i,true));
	  		}else {//CPU
  				players.put(i,new Player("CPU"+(i+1-Integer.parseInt(textPlayers.getText())),1000,i,false));
  			}
  			players.get(i).setColt(window.createText(401,301,20,20,10,players.get(i).getName()));
  	  		players.get(i).getColt().setBackground(Color.BLACK);
  	  		players.get(i).getColt().setName(players.get(i).getName());
  	  		window.addPlayFrame(players.get(i).getColt());
  		}



    	try {
        	play(window,players,Integer.parseInt(textYear.getText()));
        }catch(InterruptedException e) {
        	e.printStackTrace();
        }
    }

    //プレイ中の動作
  	private void play(Window window,Map<Integer,Player> players,int endYear) throws InterruptedException{
      	Boolean first=true;
      	int turn=0;//現在のターン

    	Player player;//操作中のプレイヤー
      	Player.setStopFlag(false);
      	while(true) {

      		if(window.monthUpdate(first,endYear))break;

      		player=players.get(turn);//このターンのプレイヤーを選定

      		window.waitButtonUpdate(player);

      		Searcher.searchShortestRoute(window,player);//目的地までの最短経路を探索
      		WaitThread waitthred  = new WaitThread(2);//再探索に対応していない為、3回程再探索を行っていた場合reloadInfoで正しく更新されない可能性がある。
      		waitthred.start();
      		waitthred.join();
      		Window.japan.saveGoal();
      		window.moveMaps();//画面遷移が少し遅い
      		window.reloadInfo();//画面上部に表示している情報を更新
      		Card.priceSort(player.getCards());//プレイヤーが持つカードを価格順にソート
      		if(!player.isPlayer()) {//cpu操作
      			player.cpu(window,players,dice,turn);
      		}else {
      			window.printMenu();
      		}

      		WaitThread turnEnd  = new WaitThread(0);//ターン終了まで待機
  			turnEnd.start();
  			turnEnd.join();
      		window.bonbyplayer(player);
      		Thread.sleep(1000);
      		Window.turnEndFlag=false;
      		Window.japan.alreadys.clear();//このターンに購入した物件リストを初期化
      	}
      	System.out.println("終わり");
      }

    private void initWindow(int endYear,int player) {
    	Window(endYear,player);
    }
    public void actionPerformed(ActionEvent e){
    	String cmd = e.getActionCommand();
    	if(cmd.equals("始める")) {
    		startFlag=true;
    	}
    }
}