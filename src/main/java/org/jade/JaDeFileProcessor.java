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
    private List<File> source;
    private File target;

    public JaDeFileProcessor(List<File> source, File target) {
        this.hasher = new JaDeHash();
        this.fileHashes = new HashSet<>();
        this.target = target;
        this.source = source;
    }

    public void processFiles() throws IOException {
        for(File dir : this.source) {
            for(File file : dir.listFiles()) {
                String hash = hasher.getHashValue(file);
                if(!this.fileHashes.contains(hash)) {
                    Files.copy(file.toPath(), this.target.toPath().resolve(file.toPath().getFileName()));
                }
                this.fileHashes.add(hash);
            }
        }
    }
}
