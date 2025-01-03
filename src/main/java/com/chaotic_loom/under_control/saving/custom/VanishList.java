package com.chaotic_loom.under_control.saving.custom;

import com.chaotic_loom.under_control.saving.ClassProvider;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class VanishList implements ClassProvider<VanishList> {
    private final List<String> playersUUIDs;

    public VanishList() {
        this.playersUUIDs = new ArrayList<>();
    }

    public VanishList(List<String> playersUUIDs) {
        this.playersUUIDs = playersUUIDs != null ? new ArrayList<>(playersUUIDs) : new ArrayList<>();
    }

    public void addUUID(String uuid) {
        if (!this.playersUUIDs.contains(uuid)) {
            this.playersUUIDs.add(uuid);
        }
    }

    public void removeUUID(String uuid) {
        this.playersUUIDs.remove(uuid);
    }

    public boolean contains(String uuid) {
        return this.playersUUIDs.contains(uuid);
    }

    public boolean contains(UUID uuid) {
        return this.playersUUIDs.contains(uuid.toString());
    }

    public boolean contains(Player player) {
        return this.playersUUIDs.contains(player.getGameProfile().getId().toString());
    }

    @Override
    public String getIdentifier() {
        return "VanishList";
    }

    @Override
    public String serialize(VanishList object) {
        return String.join(",", playersUUIDs);
    }

    @Override
    public VanishList deserialize(String value) {
        if (value == null || value.isEmpty()) {
            return new VanishList();
        }

        return new VanishList(Arrays.asList(value.split(",")));
    }
}
