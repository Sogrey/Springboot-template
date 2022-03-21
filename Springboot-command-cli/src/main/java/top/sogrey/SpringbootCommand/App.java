package top.sogrey.SpringbootCommand;

import java.util.Scanner;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	try {
            System.out.println("*****************************************");
            System.out.println("*                                       *");
            System.out.println("*     版本 V1.0.1\t\t\t*");
            System.out.println("*                                       *");
            System.out.println("*     1 选项一\t\t\t\t*");
            System.out.println("*     2 选项二\t\t\t\t*");
            System.out.println("*     0 退出程序\t\t\t*");
            System.out.println("*                                       *");
            System.out.println("*****************************************");
            boolean flag = true;
            while (flag) {
                Scanner input = new Scanner(System.in);
                System.out.println();
                System.out.print("请输入Code：");
                String a = input.nextLine();
                System.out.println("输入："+a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
