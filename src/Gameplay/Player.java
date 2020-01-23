package Gameplay;

public class Player {

    private final String player;
    private final String difficulty;
    private int healthPoints;

    public Player(String player, String difficulty) {
        this.player = player;
        this.difficulty = difficulty;
        this.healthPoints = 100;
    }

    public String getPlayer() {
        return player;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

}
