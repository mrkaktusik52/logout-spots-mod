package com.cactus;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class LogoutSpot {
    private final String name;
    private final Vec3 pos;
    private final AABB boundingBox;

    public LogoutSpot(String name, Vec3 pos) {
        this.name = name;
        this.pos = pos;

        double halfWidth = 0.3;
        this.boundingBox = new AABB(
                pos.x - halfWidth, pos.y, pos.z - halfWidth,
                pos.x + halfWidth, pos.y + 1.8, pos.z + halfWidth
        );
    }

    public String getName() { return name; }
    public Vec3 getPos() { return pos; }
    public AABB getBoundingBox() { return boundingBox; }
}