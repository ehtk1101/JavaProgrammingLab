// This class is child of ExpressWay which have clear speed

public class ExpressClear extends ExpressWay{
	private float betterRate; // This is rate for calculating plan score
	
	public ExpressClear(String name, String subname, String distance, String speed, String time) {
		super(name, subname, distance, speed, time);
		super.condition = 3; // Because the speed is very fast, the score should be high, and the role of this variable is for setting the score
		this.betterRate = Float.parseFloat(speed.replaceAll("[^0-9]", "")) * condition;  
	}
	
	public ExpressClear(ExpressWay ex) {
		super(ex.getName(), ex.getSubname(), ex.getDistance(), ex.getSpeed(), ex.getTime());
		super.condition = 3;
		this.betterRate = Float.parseFloat(ex.getSpeed().replaceAll("[^0-9]", "")) * condition;  
	}
	
	public int score() { // method for calculate and return the score
    	return (int)(betterRate * condition) / 10;
    }
}
