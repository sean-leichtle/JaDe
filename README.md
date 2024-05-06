# JaDe

JaDe is a file deduplicator providing a GUI created with JavaFX. Note that JaDe does not delete files or directories. Instead, it creates a target directory into which it deposits just one copy of every file from the source directories provided. Consequently, the target directory contains no duplicates. Whether or not the source directories are deleted is a decision left to the user.

** The present implementation of JaDe does not allow for the consolidation of subdirectories and will not function as expected if a directory contains subdirectories.**