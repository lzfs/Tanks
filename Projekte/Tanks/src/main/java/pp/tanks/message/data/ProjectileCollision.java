package pp.tanks.message.data;

public class ProjectileCollision implements Comparable<ProjectileCollision>{
    public final int id1;
    public final int id2;
    public final int dmg1;
    public final int dmg2;
    public final boolean dest1;
    public final boolean dest2;
    public final long serverTime;

    public ProjectileCollision(int id1, int id2, int dmg1, int dmg2, boolean dest1, boolean dest2, long serverTime) {
        this.id1 = id1;
        this.id2 = id2;
        this.dmg1 = dmg1;
        this.dmg2 = dmg2;
        this.dest1 = dest1;
        this.dest2 = dest2;
        this.serverTime = serverTime;


    }

    /**
     * uses the compareTo-methode of Priority-queues
     * @param o given serverTime
     * @return returns 1 or -1 depending which is time is bigger
     */
    @Override
    public int compareTo(ProjectileCollision o) {
        return Long.compare(this.serverTime, o.serverTime);
    }
}
