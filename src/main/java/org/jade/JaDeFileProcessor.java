package org.jade;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JaDeFileProcessor {

    private JaDeHash hasher;
    private Set<String> fileHashes;
    private List<File> origin;
    private File destination;

    public JaDeFileProcessor(List<File> origin, File destination) {
        this.hasher = new JaDeHash();
        this.fileHashes = new HashSet<>();
        this.destination = destination;
        this.origin = origin;
    }

    public void processFiles() throws IOException {
        for(File dir : this.origin) {
            for(File file : dir.listFiles()) {
                String hash = hasher.getHashValue(file);
                if(!this.fileHashes.contains(hash)) {
                    Files.copy(file.toPath(), destination.toPath().resolve(file.toPath().getFileName()));
                }
                this.fileHashes.add(hash);
            }
        }
    }
}
