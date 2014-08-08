PostOffice
===========

PostOffice is a Bukkit plugin that help players on sending their mails or packages.

Feature
-------------
- Players can setup their "MailBox" by command.
- All the Mails and Packages will be sent into the Mailbox when there is enough space in the Chest.
- When the Chest is full, rest of the Mails and Packages will be placed in Post Office(Queue), and player will get message reminding them to clear their mailbox.

Working in Progress
-------------
- Mail can be send from WebUI.

Command
-------------
- /po setmailbox - Register mailbox (Player need to point at a chest.)
- /po resetmailbox - Unregister mailbox.
- /po send <receiver> - Send mail or any item to receiver.
- /po check - Check how many items in your mailbox or queue.
- /po pull - Pull mail from queue to your mailbox.
- /po whereismymailbox - Where is my mailbox?
- /po reload - Reload plugins.

Permission
-------------
- postoffice.sendmail - Allow player to send mails. (Default)
- postoffice.setmailbox - Allow player to set and reset mailbox. (Default)
- postoffice.reload - Allow player to reload plugin. (Op)

This is a work done by LongCat Lab.