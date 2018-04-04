package sw_count_word;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileFilter;
import java.util.*;
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  

public class main {
	
	public static boolean isline = false;
	public static boolean ischar = false;
	public static boolean isword = false;
	public static boolean isofunc= false;
	public static boolean isafunc= false;
	public static boolean issfunc= false;
	public static boolean isefunc= false;
	
	public static void main(String[] args) throws Exception {
		  // 统计一个文件的字符数，单词数，行数
		//Scanner input = new Scanner(System.in);
		  System.out.println("wc.exe ");
		  //String read= input.nextLine();
		  String path = null;
		  String name = null;
		  String stl = null;
		  int i=0;
		  for(String arg:args)
		  { 
			  if(args[i].equals("-c"))
				ischar = true;
			  else if(args[i].equals("-w"))
				isword = true;
			  else if(args[i].equals("-l"))
				isline= true;
			  else if(args[i].equals("-s"))
				issfunc= true;
			  else if(args[i].equals("-a"))
				isafunc= true;  
			  else if(args[i].equals("-e"))
				isefunc= true; 
			  else if(args[i].equals("-o"))
				isofunc= true; 
			  else if(args[i-1].equals("-o"))
				name = args[i];
			  else if(args[i-1].equals("-e"))
				stl = args[i];
			  else 
				path = args[i];
			  i++; 
			 }
		  if(isofunc&&!issfunc){
			  writefunc(name,path,stl);
		  }
		  else if(!isofunc&&!issfunc) {
			  writefunc("result.txt",path,stl);
		  }
		  else if(isofunc&&issfunc) {
			  if(path.indexOf('\\')>-1) {
				  String ep = path.substring(0,path.lastIndexOf('\\'));
				  String ftype=path.substring(path.lastIndexOf('.'),path.length());
				  Sfunc(ep,ftype,name,stl);
			  }
			  else {
				  String ftype=path.substring(path.lastIndexOf('.'),path.length());
				  Sfunc(".",ftype,name,stl);
			  }
		  }
		  else
		  {
			  if(path.indexOf('\\')>-1) {
				  String ep = path.substring(0,path.lastIndexOf('\\'));
				  String ftype=path.substring(path.lastIndexOf('.'),path.length());
				  Sfunc(ep,ftype,"result.txt",stl);
			  }
			  else {
				  String ftype=path.substring(path.lastIndexOf('.'),path.length());
				  Sfunc(".",ftype,"result.txt",stl);
			  }
		  }
		 
	
		 
	}
		  
		  
		  
	//数字符
	public static int Ccount(String path)
	{
		int Cn=0;
		try {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
		BufferedReader br = new BufferedReader(isr);
		while(br.read()!=-1)
		{
			String s = br.readLine();
			Cn += s.length();//字符个数就是字符长度
			Cn++;
		}
		System.out.println("字符数"+Cn);
		br.close();
		return Cn;
		}
		catch(IOException e) {
			 e.printStackTrace();
			 return 0;
		}
	}
	//数单词
	public static int Wcount(String path)
	{
		int Wn=0;
		try {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
		BufferedReader br = new BufferedReader(isr);
		while(br.read()!=-1){
			String s1 =br.readLine();
			Scanner s2 = new Scanner(s1).useDelimiter(" +|,+|\\?+|\\.+");
			while(s2.hasNext()){
				Wn++;
				s2.next();
			}
		}
		
		 System.out.println("单词数"+Wn);
		 br.close();
		 return Wn;
		 }catch(IOException e) {
			 e.printStackTrace();
			 return 0;
		}
		
	}
	//数行
	public static int Lcount(String path)
	{
		int Ln=0;
		try {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
		BufferedReader br = new BufferedReader(isr);
		while(br.read()!=-1)
		{
			Ln++;//行数
		}
		
		 System.out.println("行数"+Ln);
		br.close();
		return Ln;
		}catch(IOException e) {
			 e.printStackTrace();
			 return 0;
		}
		
	}
	
	//写入文件
	public static void writefunc(String filename,String path,String stl)
	{
		try {
		File writefile = new File(filename);
		if(!writefile.exists())
			writefile.createNewFile();
		BufferedWriter out = new BufferedWriter(new FileWriter(writefile)); 
		if(ischar) 
		{
			int Cn = Ccount(path);
			out.write(path + ", 字符数："+ Cn+ "\r\n");
			}
		if(isword)
		{
			if(!isefunc){
				int Wn = Wcount(path);
				out.write(path + ", 单词数：" + Wn + "\r\n");
				}
			else {
				int Wn = Efunc(stl,path);
				out.write(path + ", 停用单词数：" + Wn + "\r\n");
			}
		}
		if(isline) 
		{
			int Ln = Lcount(path);
			out.write(path + ", 行数："+ Ln + "\r\n");
			}
		if(isafunc)
		{
			int L[] = Afunc(path);
			out.write(path + ", 代码行/空行/注释行: " +L[0]+"/"+L[1]+"/"+L[2]+"\r\n");
		}
		
		out.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//扩展功能
	//递归寻找所有文件
	 public static void Sfunc(String fileDir,String ftype,String filename,String stl) {  
	        List<File> fileList = new ArrayList<File>();  
	        File file = new File(fileDir);  
	        File[] files = file.listFiles();// 获取目录下的所有文件或文件夹  
	        if (files == null) {// 如果目录为空，直接退出  
	            return;  
	        }  
	        // 遍历，目录下的所有文件，挑选所有合适的文件 
	        for (File f : files) {  
	            if (f.isFile()) {//是文件，下一步判断  
	            	String fname = f.getName();
	            	if(fname.endsWith(ftype)) {//后缀名合格，加入list
	            		fileList.add(f);}  
	            }
	            else if (f.isDirectory()) {  //是文件夹，递归求这个文件夹下的文件和文件夹
	                System.out.println(f.getAbsolutePath());  
	                Sfunc(f.getAbsolutePath(),ftype,filename,stl);  
	            }  
	        }  
	        //继续处理，根据输入判断一下处理方式，没什么好说的
	        int m = fileList.size();
	        int i =0;
	       for(File single:fileList) {
	        	String path2 = single.getAbsolutePath();
	        	if(isofunc)
	        		writefunc(filename,path2,stl);
	        	else
	        		writefunc("result.txt",path2,stl);
	        }
	    }  
	  
	 //停用词表
	 public static int Efunc(String slt,String path) {
		 int cnum=0;
		 try {
			 //读停用词表
				InputStreamReader isr = new InputStreamReader(new FileInputStream(slt));
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				 TreeMap<String,Integer> tm = new TreeMap<String,Integer>();//构建停用词树
				 //读取文件
				 while((line=br.readLine())!=null){
				    
				    line.toLowerCase();//全小写
				    String str[] = line.split("\\s+|\\?+|\\.+|,+|，+");//分割停用词
				    for(String s: str) {//遍历
				    	if(!tm.containsKey(s))//如果树里没有这个单词
				    	{
				    		tm.put(s,1);//加进去
				    	}
				    }
				    }
				 br.close();
				 //读文件
				 InputStreamReader isr2 = new InputStreamReader(new FileInputStream(path));
				 BufferedReader br2 = new BufferedReader(isr2);
				 String line2 = null;
				 while((line2=br.readLine())!=null){
					 line2.toLowerCase();
					 String str[] = line2.split("\\s+|\\?+|\\.+|,+|，+");//分割处理文件单词
					 for(String s: str) {
						 if(!tm.containsKey(s)){//如果这个单词不在屏蔽树表里
							 cnum++;//单词数++
							 }
						 }
					 }
				 br2.close();
				 return cnum;
				 }catch(IOException e) {
					 e.printStackTrace();
					 return 0;
					 }
		 }
	 
	 //更复杂的数据
	 public static int[] Afunc(String path) {
		 String regxNodeBegin = "\\s*//[\\s\\S]*";
		// String regxNodeEnd = ".*\\*/\\s*";
	    // String regx = "//.*";
		 String regx ="\\s*\\}//[\\s\\S]*"; 
	     String regxSpace = "\\s*";
	     int Lblank=0,Lcode=0,LNode=0;
		 try {
			 InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
			 BufferedReader br = new BufferedReader(isr);
			 String line = null;
			 while((line = br.readLine()) != null) {
				 if(line.matches(regxNodeBegin))
					 LNode++;
				 else if (line.matches(regx))
					 LNode++;
				 else if(line.matches(regxSpace))
					 Lblank++;
				 else
					 Lcode++;
			 }
			 br.close();
			 int L[]= {Lcode,Lblank,LNode};
			 return L;
		 }catch(IOException e) {
			 e.printStackTrace();
			 return null;
		 }
	 }
	 
	 
	 
	 
	//备用函数
	//统计空格数
	public static int countSpace(String str) {  
		int count = 0;  
	    Pattern p = Pattern.compile("\\s");  
	    Matcher m = p.matcher(str);  
	    while(m.find())
	    {  
	        count++;
	        } 
	    return count;  
	    }  
	//统计逗号数
	public static int countSign(String str) {  
        int count = 0;  
        Pattern p = Pattern.compile(",");  
        Matcher m = p.matcher(str);  
        while(m.find()){  
            count++;  
        }  
        return count;  
    }  

}
	