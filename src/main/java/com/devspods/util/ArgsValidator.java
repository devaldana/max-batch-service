package com.devspods.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;

import java.io.File;
import java.nio.file.*;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
public final class ArgsValidator {
    private ArgsValidator(){}

    public static void validateArgs(final ApplicationArguments args) {
        printHeader();
        validateArgsNames(args);
        validateArgsValues(args);
        printFooter();
    }

    private static void validateArgsNames(final ApplicationArguments args){
        log.info("Validating args names...");
        var argsNames = args.getOptionNames();
        if(!argsNames.contains(Constants.FILES_FOLDER_PATH_ARG)) throw new IllegalArgumentException("At least one argument is missing.");
    }

    private static void validateArgsValues(final ApplicationArguments args) {
        log.info("Validating args values...");
        var filesFolderPath = args.getOptionValues(Constants.FILES_FOLDER_PATH_ARG);
        validateArgsLists(filesFolderPath);
        validateFilesFolderPath(filesFolderPath.get(0));
    }

    private static void validateArgsLists(final List<String> filesFolderPath) {
        if(invalidArgList(filesFolderPath)) {
            throw new IllegalArgumentException("Invalid argument: repeated argument, please review the inputs.");
        }
    }

    private static boolean invalidArgList(final List<?> list){
        return isEmpty(list) || list.size() > 1;
    }

    private static void validateFilesFolderPath(final String filesFolderPath) {
        log.info("Validating filesLocation argument: {}", filesFolderPath);
        try {
            validateFileIsAccessible(filesFolderPath, Constants.ARTISTS_FILE_NAME);
            validateFileIsAccessible(filesFolderPath, Constants.GENRES_FILE_NAME);
            validateFileIsAccessible(filesFolderPath, Constants.ARTISTS_GENRES_FILE_NAME);
        } catch (InvalidPathException exception) {
            throw new IllegalArgumentException("Error reading files: make sure the folder exists and the files have the required names", exception);
        }
    }

    private static void validateFileIsAccessible(final String filesFolderPath, final String fileName) {
        var file = Paths.get(filesFolderPath + File.separator + fileName).toFile();
        if(!file.isFile()) throw new IllegalArgumentException(file.getAbsolutePath() + " is not a file");
        if(!file.canRead()) throw new IllegalStateException(file.getAbsolutePath() + " can not be read");
    }

    private static void printHeader() {
        log.info("ArgsValidator initialized\n\n:. Starting validation of arguments...\n");
    }

    private static void printFooter() {
        log.info("Finishing validations...\n\n.: Arguments successfully validated!\n");
    }
}