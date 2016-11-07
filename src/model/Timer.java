package model;

public class Timer {
	
	private int time=180;
	public long startGameTime = System.currentTimeMillis();
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public void countDown(){
		
		this.time = 180 - (int) (System.currentTimeMillis() - startGameTime) / 1000;
		//System.out.println(time);
		if (this.time <= 0) {
			this.time = 0;
		}
	}
}
