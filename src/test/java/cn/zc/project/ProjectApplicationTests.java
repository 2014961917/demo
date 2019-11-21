package cn.zc.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class ProjectApplicationTests {

    @Value("${media.chunkSize}")
    private int chunkSize;
    @Test
    void contextLoads() {
    }


    /**
     * 测试文件分块
     *
     * @throws IOException
     */
    @Test
    void testChunk() throws IOException {
        File sourceFile = new File("F:\\test\\11-媒资管理-上传文件-文件分块测试.avi");

        String chunkPath = "F:\\test\\chunk\\";
        File chunkFolder = new File(chunkPath);

        if (!chunkFolder.exists()) {
            boolean mkdirs = chunkFolder.mkdirs();

            System.out.println(mkdirs);
            if (!mkdirs) {
                System.out.println(chunkPath + "文件夹创建不成功！");
                System.exit(500);
            }
        }
        /*//分块大小 1M
        long chunkSize = 1024 * 1024;*/
        //分块数量
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        /*if (chunkNum <= 0){
            chunkNum = 1;
        }*/
        //缓冲区大小
        byte[] b = new byte[1024];
        //使用RandomAccessFile访问文件
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");
        //文件分块
        for (int i = 0; i < chunkNum; i++) {
            //创建分块文件
            File file = new File(chunkPath + i);
            boolean newFile = file.createNewFile();
            if (newFile) {
                //向分块文件中写数据
                RandomAccessFile raf_write = new RandomAccessFile(file, "rw");
                int len = -1;
                while ((len = raf_read.read(b)) != -1) {
                    raf_write.write(b, 0, len);
                    if (file.length() > chunkSize) {
                        break;
                    }
                }
                raf_write.close();
            }
        }
        raf_read.close();
    }


    @Test
    void testMerge() throws IOException {
        //块文件目录
        File chunkFolder = new File("F:\\test\\chunk\\");
        //合并文件
        File mergeFile = new File("F:\\test\\1.avi");
        if (mergeFile.exists()) {
            boolean delete = mergeFile.delete();
            if (!delete) {
                System.out.println("合并文件已存在，并且删除失败! 请尝试手动删除");
                System.exit(500);
            }
        }
        //创建新的合并文件
        boolean newFile = mergeFile.createNewFile();
        if (!newFile) {
            System.out.println("尝试创建合并文件失败");
            System.exit(500);
        }
        //用于写文件
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
        //指针指向文件顶端
        raf_write.seek(0);
        //缓冲区
        byte[] b = new byte[1024];
        //分块列表
        File[] fileArray = chunkFolder.listFiles();
        if (fileArray == null || fileArray.length <= 0) {
            System.out.println("文件块读取错误或文件块不存在");
            System.exit(500);
        }
        //转成集合，便于排序
        List<File> filesList = new ArrayList<>(Arrays.asList(fileArray));
        filesList.sort((o1, o2) -> {
            if (Integer.parseInt(o1.getName()) == Integer.parseInt(o2.getName())) {
                throw new RuntimeException(chunkFolder+"中的块文件出错");
            } else if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                return 1;
            }else if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
                return -1;
            }
            return 0;
        });
        //查看块文件集合的排序情况
        filesList.forEach(System.out::println);
        //合并文件
        for (File chunkFile : filesList) {
            int len = -1;
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile,"r");
            while ((len = raf_read.read(b)) != -1){
                raf_write.write(b,0,len);
            }
            raf_read.close();
        }
        raf_write.close();


    }



    @Test
    void exerciseChunk() throws IOException{
        File sourceFile = new File("F:\\test\\11-媒资管理-上传文件-文件分块测试.avi");

        int chunkNum = (int)Math.ceil(sourceFile.length() * 1.0 / chunkSize);

        RandomAccessFile raf_read = new RandomAccessFile(sourceFile,"r");
        byte[] b = new byte[1024];
        for (int i = 0; i < chunkNum; i++) {
            int len = -1;
            File chunkFile = new File("F:\\test\\chunk\\"+ i);
            chunkFile.createNewFile();
            RandomAccessFile raf_write = new RandomAccessFile(chunkFile,"rw");
            while ((len = raf_read.read(b)) != -1){
                raf_write.write(b,0,len);
                if (chunkFile.length() > chunkSize){
                    break;
                }
            }
            raf_write.close();
        }
        raf_read.close();
    }
}
