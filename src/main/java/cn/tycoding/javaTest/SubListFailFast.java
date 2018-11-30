package cn.tycoding.javaTest;

import java.util.ArrayList;
import java.util.List;

public class SubListFailFast {
    public static void main(String[] args) {
        List master=new ArrayList();
        master.add("one");
        master.add("two");
        master.add("three");
        master.add("four");
        master.add("five");
        List branchList=master.subList(0,3);//取0,1,2
        System.out.println(master);
//        master.remove(0);
//        master.add("ten");
//        master.clear();

        branchList.clear();
        branchList.add("six");
        branchList.add("seven");
        branchList.remove(0);
        System.out.println(master);
        for(Object t:branchList){
            System.out.println(t);
        }
        System.out.println(master);

        List<String> list=new ArrayList<String>();
        list.add("one");
        list.add("two");
        list.add("three");
        for(String s:list){
            if("two".equals(s)){
                list.remove(s);
            }
        }
        System.out.println(list);
    }
}
