package io.github.michaelfedora.fedoraseconutil.cmdexecutors.fedoraseconutil;

import io.github.michaelfedora.fedoraseconutil.PluginInfo;
import io.github.michaelfedora.fedoraseconutil.cmdexecutors.FeuExecutorBase;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 3/19/2016.
 */
public class FeuFaqExecutor extends FeuExecutorBase {

    public static final List<String> ALIASES = Arrays.asList("faq", "wiki");

    public static final String NAME = ALIASES.get(0);

    public static CommandSpec create(Map<List<String>, ? extends CommandCallable> children) {
        return CommandSpec.builder()
                .description(Text.of("Lists FAQ on how to use this plugin"))
                .permission(PluginInfo.DATA_ROOT + ".faq")
                .children(children)
                .executor(new FeuFaqExecutor())
                .build();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        // TODO implement
        src.sendMessage(Text.of(TextColors.RED, TextStyles.BOLD, "Not implemented :L"));

        return CommandResult.success();
    }
}
