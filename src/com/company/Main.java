package com.company;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Main {

    public static final String root = ".\\Games\\";

    public static String userName() throws ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        String osName = System.getProperty("os.name").toLowerCase();
        String className = null;
        String methodName = "getUsername";
        String userName = "unknown";

        if (osName.contains("windows")) {
            className = "com.sun.security.auth.module.NTSystem";
            methodName = "getName";
        } else if (osName.contains("linux")) {
            className = "com.sun.security.auth.module.UnixSystem";
        } else if (osName.contains("solaris") || osName.contains("sunos")) {
            className = "com.sun.security.auth.module.SolarisSystem";
        }

        if (className != null) {
            Class<?> c = Class.forName(className);
            Method method = c.getDeclaredMethod(methodName);
            Object o = c.newInstance();
            userName = method.invoke(o).toString();
        }
        return userName;
    }

    public static String createStructure(String path, Boolean fileTrueDirFalse) {
        String userName = "Имя пользователя не определено";
        String fullPath = root + path;

        try {
            userName = userName();
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (fileTrueDirFalse) {
            File file = new File(fullPath);

            try {
                if (file.createNewFile()) {
                    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy hh:mm:ss")) +
                            "-Создан файл-" + file.getName() + "-в разделе-"
                            + fullPath + "-пользователь-" + userName + "%";
                } else return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy hh:mm:ss")) +
                        "-Ошибка при создании файла, возможно файл создан ранее" + " в разделе "
                        + fullPath + "-пользователь-" + userName + "%";

            } catch (IOException e) {
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy hh:mm:ss"))
                        + "-Ошибка при создании файла-" + file.getName() + "-" + e.getMessage() + "-в разделе-" + fullPath +
                        "-пользователь-" + userName + "%";
            }
        } else {
            File dir = new File(fullPath);
            if (dir.mkdir()) {
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy hh:mm:ss")) +
                        "-Создан" + "-раздел-" + fullPath + "-имя пользователя-" + userName + "%";
            } else return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yy hh:mm:ss"))
                    + "-Ошибка при создании раздела-" + fullPath + "-пользователь-" + userName + "%";
        }
    }

    public static void main(String[] args) {

        StringBuilder log = new StringBuilder();

        log.append(createStructure("src", false));
        log.append(createStructure("res", false));
        log.append(createStructure("savegames", false));
        log.append(createStructure("temp", false));

        log.append(createStructure("src\\main", false));
        log.append(createStructure("src\\test", false));

        log.append(createStructure("src\\main\\Main.java", true));
        log.append(createStructure("src\\main\\Utils.java", true));

        log.append(createStructure("res\\drawables", false));
        log.append(createStructure("res\\vectors", false));
        log.append(createStructure("res\\icons", false));

        log.append(createStructure("temp\\temp.txt", true));

        String[] arraysLog = log.toString().split("%");
        for (String string : arraysLog
        ) {
            System.out.println(string);
        }
        Arrays.toString(log.toString().split("%"));


    }
}
