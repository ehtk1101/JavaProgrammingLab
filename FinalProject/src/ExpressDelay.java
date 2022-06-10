// This class is child of ExpressWay which have speed which is very near from 0.

public class ExpressDelay extends ExpressWay{
	private float betterRate; // This is rate for calculating plan score
	
	public ExpressDelay(String name, String subname, String distance, String speed, String time) {
		super(name, subname, distance, speed, time);
		super.condition = (float) 0.1; // Because the speed is almost stopped status, the average score should be declined, and the role of this variable is for setting the score
		this.betterRate = Float.parseFloat(speed.replaceAll("[^0-9]", "")) * condition;
	}

	public ExpressDelay(ExpressWay ex) {
		super(ex.getName(), ex.getSubname(), ex.getDistance(), ex.getSpeed(), ex.getTime());
		super.condition = (float)0.1;
		this.betterRate = Float.parseFloat(getSpeed().replaceAll("[^0-9]", "")) * condition;
	}
	
	public int score() { // method for calculate and return the score
		return (int)(betterRate * condition) / 10;
    }
}
