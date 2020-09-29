package lifegame.game.object.map.print.animation.model;

public class AnimationThreadModel extends Thread {
	protected static boolean animationFlag;
	public AnimationThreadModel(){
		animationFlag=false;
	}
	public static void end() {
		animationFlag = true;
	}
	public static boolean isEnd() {
		return animationFlag;
	}
}
