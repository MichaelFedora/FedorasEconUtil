package io.github.michaelfedora.fedoraseconutil.listeners;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

/**
 * Created by Michael on 8/20/2016.
 */
public class BlockPlaceListener {

    @Listener
    public void onBlockPlace(ChangeBlockEvent.Place event, @First Player player) {
        BlockType type = event.getTransactions().get(0).getFinal().getState().getType();
        if(!(type == BlockTypes.STANDING_SIGN || type == BlockTypes.WALL_SIGN)) return;

        // adjust formatting, or make it error-formatted if it isn't properly done :< (don't override other plugins though!)
    }
}
