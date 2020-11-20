package pp.tanks.model;

import pp.tanks.message.data.ProjectileCollision;

public interface ICollisionObserver {

    void notify(ProjectileCollision coll);
}
