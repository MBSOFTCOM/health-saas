package cn.iocoder.yudao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class StringToFile {
    public static void main(String[] args) {
        /*for (int i = 0; i < 3; i++) {
            StringToFile.write(i+1,2000);
        }*/
//        StringToFile.copyImg(5000);
        StringToFile.write(4,5000);
    }
    public static String getStart(int index){
        switch (index){
            case 1 : return "insert into tb_screen_person(id,idNum,screenType,year,screenPoint,deptId) values";
            case 2 : return "insert into tb_screen_sum(personId,idNum,screenType,year,curFinish,collectId,collectNum,statusFlag) values";
            case 3 : return "insert into tb_screen_collect(personId,idNum,statusFlag,screenType,year) values";
            case 4 : return "insert into tb_screen_images(personId,idNum,statusFlag,screenType,year,path,type) values";
        }
        return "";
    }
    public static void write(int index,int length){
        String fileName = "";
        switch (index){
            case 1: fileName = "e://person.txt";break;
            case 2: fileName = "e://sum.txt";break;
            case 3: fileName = "e://collect.txt";break;
            case 4: fileName = "e://image.txt";break;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String start=StringToFile.getStart(index);
            writer.write(start);
            // 循环生成1000行字符串
            for (int i = 1; i <= length; i++) {
                String line ="";
                switch (index){
                    case 1: line=String.format("(%d,36073019800324%04d,2,2024,'舍得离开房间科索沃独立',117),", i,i);break;
                    case 2: line = String.format("(%d,36073019800324%04d,2,2024,'采集组',%d,1,1),", i,i,i+1);break;
                    case 3: line = String.format("(%d,36073019800324%04d,1,2,2024),", i,i);break;
                    case 4: line = String.format("(%d,36073019800324%04d,1,2,2024,'_doc/uniapp_save/image_%04d.jpg',%d),", i,i,i,8);break;
                }
                writer.write(line);
                writer.newLine(); // 添加换行符
            }
            System.out.println("成功写入到 " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //复制指定路径的图片，生成5000张图片副本在同一个文件夹中，文件名以后缀数字区分
    public static void copyImg(Integer num){
        // 源文件对象
        File source = new File("C:\\Users\\lenovo\\Desktop\\新建文件夹\\ppsource.jpg");
// 目标文件对象
        File targetDir = new File("E:\\copyImg\\copy");


        // 源图片路径
        String sourceImagePath = "C:\\Users\\lenovo\\Desktop\\新建文件夹\\ppsource.jpg";
        // 目标目录路径
        String targetDirStr = "E:\\copyImg";

        try {
            // 创建目标目录（如果不存在）
            Files.createDirectories(Paths.get(targetDirStr));

            // 复制图片并指定不同的文件名
            for (int i = 1; i <= num; i++) {
                // 生成新的文件名
                String newFileName = "image_" + String.format("%04d", i) + ".jpg";
                Path destinationPath = Paths.get(targetDirStr, newFileName);

                // 复制文件
                Files.copy(Paths.get(sourceImagePath), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("All images copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

