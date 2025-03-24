import java.util.*;
 class Test4 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        String[] in = line.split(",");
        Set<Integer> set = new HashSet<>();
        int start = 0;
        int len = 0;
        for(int i = 0; i < in.length; i++){
            set.add(Integer.parseInt(in[i]));
        }
        for(int x : set){
            if(set.contains(x - 1)) continue;
            int end = x + 1;
            int tmpLen = 1;
            while(set.contains(end)){
                tmpLen++;
                end++;
            }
            if(tmpLen > len){
                len = tmpLen;
                start = x;
            }
        }
        System.out.println(start + "," + len);
    }
}
