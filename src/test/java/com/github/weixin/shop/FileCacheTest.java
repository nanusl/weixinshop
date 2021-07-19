package com.github.weixin.shop;

import cn.hutool.core.io.NioUtil;
import cn.hutool.core.util.HashUtil;
import cn.hutool.core.util.URLUtil;
import com.google.common.hash.HashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author nanusl
 * @version V1.0
 * @Description
 * @date 2021-02-07 15:06
 */
@Slf4j
public class FileCacheTest {

    @Test
    void urlTest() throws IOException {

        File file = getFile("http://commission-dev.oss-cn-shanghai.aliyuncs.com/task/WYE050303/banner/yw6PpEnCQ21528281468843.png");

        System.out.println("path = " + file);
        System.out.println("absolutePath = " + file.getAbsolutePath());
    }

    public static File getFile(String url) throws IOException {

        String fileName = getFileName(url),
                hashPath = getHashPath(fileName);

        Path directoryPath = Paths.get("test_file", File.separator, hashPath, File.separator);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        Path filePath = Paths.get(directoryPath.toString(), fileName);
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            Files.deleteIfExists(filePath);
            Files.createFile(filePath);

            URL _url = URLUtil.toUrlForHttp(url);
            ReadableByteChannel readableByteChannel = Channels.newChannel(_url.openStream());

            FileChannel outFileChannel = FileChannel.open(filePath, StandardOpenOption.WRITE);
            NioUtil.copy(readableByteChannel, outFileChannel);

            NioUtil.close(readableByteChannel);
            NioUtil.close(outFileChannel);
        }
        return filePath.toFile();
    }

    private static String getFileName(String urlStr) {
        return urlStr.substring(urlStr.lastIndexOf("/") + 1);
    }

    private static String getHashPath(String fileName) {
        String fileIdStr = StringUtils.substringBeforeLast(fileName, ".");

        int hash = fileIdStr.hashCode();
        HashCode hashCode = HashCode.fromInt(hash);

        StringBuilder sb = new StringBuilder();
        for (byte b : hashCode.asBytes()) {
            if (0 != b) {
                sb.append(String.format("%02X" + File.separator, b));
            }
        }

        return sb.toString();
    }

}
