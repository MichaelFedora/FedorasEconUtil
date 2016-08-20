package io.github.michaelfedora.fedoraseconutil.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

/**
 * Created by Michael on 8/20/2016.
 */
public class BlockPlaceListener {

    @Listener
    public void onBlockPlaced(ChangeBlockEvent.Place event, @First Player player) {

    }
}
