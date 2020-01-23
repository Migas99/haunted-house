package HauntedHouse;

public class Player {

    private final String player;
    private final int difficulty;
    private double healthPoints;

    public Player(String player, int difficulty) {
        this.player = player;
        this.difficulty = difficulty;
        this.healthPoints = 100;
    }

    public String getPlayer() {
        return player;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public double getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

}
