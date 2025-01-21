package com.lotterydev.util;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Misc {
    public static String getCodeFromFile(Path filePath) throws IOException {
        Counter lineCounter = new Counter();
        return Files.lines(filePath)
                .map(line -> String.format("%d. %s", lineCounter.incrementAndGet(), line))
                .collect(Collectors.joining("\n"));
    }

    public static void reloadFileFromDisk(Path filePath) {
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByNioFile(filePath);
        if (virtualFile == null) {
            throw new RuntimeException(String.format("Cannot find \"%s\"", filePath));
        }
        virtualFile.refresh(false, false);
    }
}
