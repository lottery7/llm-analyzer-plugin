package com.lotterydev.util;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Misc {
    public static String getEnumeratedCodeFromFile(Path filePath) throws IOException {
        AtomicInteger lineCounter = new AtomicInteger();
        return Files.lines(filePath)
                .map(line -> String.format("%d. %s", lineCounter.incrementAndGet(), line))
                .collect(Collectors.joining("\n"));
    }

    public static void reloadFileFromDisk(Path filePath) throws FileNotFoundException {
        VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(filePath);
        if (virtualFile == null) {
            throw new FileNotFoundException(String.format("Cannot find \"%s\"", filePath));
        }
    }
}
