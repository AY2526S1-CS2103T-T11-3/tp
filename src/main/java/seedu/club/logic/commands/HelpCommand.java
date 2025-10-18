package seedu.club.logic.commands;

import seedu.club.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    @Override
    // This makes the app go to memberList instead of eventList, need to change this someday
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE,
                true, false, false);
    }
}
