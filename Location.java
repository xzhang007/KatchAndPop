public class Location {
	private int x;
	private int y;
	private String thing;
	private int number;
		
	public Location(int x, int y, String thing) {
		this.x = x;
		this.y = y;
		this.thing = thing;
	}
	
	public Location(int x, int y, String thing, int number) {
		this(x, y, thing);
		this.number = number;
	}
		
	public int getX() {
		return x;
	}
		
	public int getY() {
		return y;
	}
	
	public String getThing() {
		return thing;
	}
	
	public int getNumber() {
		return number;
	}
}