// This class is child of ExpressWay which have slow speed

public class ExpressSlow extends ExpressWay{
	private float betterRate; // This is rate for calculating plan score
	
	public ExpressSlow(String name, String subname, String distance, String speed, String time) {
		super(name, subname, distance, speed, time);
		super.condition = 1; // Because the speed is very slow, the score should be less than when speed is fast, and the role of this variable is for setting the score
		this.betterRate = Float.parseFloat(speed.replaceAll("[^0-9]", "")) * condition;
	}
	
	public ExpressSlow(ExpressWay ex) {
		super(ex.getName(), ex.getSubname(), ex.getDistance(), ex.getSpeed(), ex.getTime());
		super.condition = 1;
		this.betterRate = Float.parseFloat(getSpeed().replaceAll("[^0-9]", "")) * condition;
	}

	public int score() { // method for calculate and return the score
		return (int)(betterRate * condition) / 10;
    }
}
