# java-io-demo


| Input | | Output| |
|--------|-----|-------| ------------|
| Character | Bytes | Character | Bytes |
| Reader | InputStream | Writer | OutputStream |

#Utility Classes
| File | Path (Java 7, NIO) |
|-------|------------|
|| Both models file or directory on the disk |
|File file = new File("files.txt")| where does|
- file.exists()
- isDirectory()
- isFile()
- canRead()
- canWrite()
- canExecute()

- createNewFile()
- mkdir()
- mkdirs()

- delete()
- deleteOnExit()
- renameTo("file2.txt")

- getName()
- getParent()
- getPath()
- getAbsolutePath() - Uses OS file system
- getCanonicalPath() - Uses OS file system

Path can
- get attributes
- check for file system events
- normalize(): removes redundant elements
- toAbsolutePath()
- toRealPath(): Resolves the symbolic links
- Files.isSame(path1, path2)
- resolve()

Path root = Paths.get("files");
Path child = Paths.get("data.txt");

Path resolved = root.resolve(child); //files/data.txt

if the child path is absolute path then resolve will just return the absolute path
- resolveSibling()

- relativize(): creating a relative path from one to the other
Path dir = Paths.get("src/main/java");
Path file = Paths.get"src/main/java/com/example/Main.java");

Path relative = dir.relativize(file); /com/example/Main.java

Path dir = Paths.get("src/java");
Path file =  Paths.get("projects/src/java/com/example/Main.java");

Path relative = dir.relativize(file); // ..\..\project\src\java\com\example\Main.java
If parent directories are not the same and if paths are not absolute, paths are resolved using ..\..\
i.e
- if both paths are absolute
- if both paths are relative- relativize(): creating a relative path from one to the other
- if one path is absolute and other is not, an IllegalArgumentException


Reading Characters

Reader

- Reading a Single Character
```java
    int nextChar = reader.read(); //when there are no more chars it returns -1
    while(nextChar != -1){
        System.out.print((char)nextChar);
        nextChar = reader.read();
    }
```

- Reading an array of Characters
```java
    char[] buffer = new char[1024];

    int number = reader.read(buffer); //where there are no more chars to read it returns -1
    //NOTE: number is the number of character that have been read, it is not 1024(size of the array)
    while(number != -1){
        System.out.println(Arrays.toString(buffer));
        number = reader.read(buffer);
    }
```
```java
    char[] buffer = new char[1024];

    int number = reader.read(buffer,5,10); //where there are no more chars to read it returns -1
    //NOTE: number is the number of character that have been read, it is not 1024(size of the array)
    while(number != -1){
        System.out.println(Arrays.toString(buffer,5,10));
        number = reader.read(buffer);
    }
```

- Handling exception & Closing reader
    - Java 1 way
```java
    Reader reader = null;
    try{
        reader = ... //construction of reader throws checked exception
        int nextChar = reader.read(); //throws exception
        while(nextChar != -1){
            System.out.print((char)nextChar);
            nextChar = reader.read(); //throws exception
        }
    } catch(Exception e){
    
    } finally {
        if(reader!=null) { //in the case where constructor of the reader throws exception
            try{
                reader.close()
            } catch (IOException e){
                //log the exception
            }
        }
    }
    
```
    - Java 7 - try-with-resources - Closing resources that extend Autoclosable interface
```java
    try (Reader reader = ... ){
        int nextChar = reader.read(); //throws exception
        while(nextChar != -1){
            System.out.print((char)nextChar);
            nextChar = reader.read(); //throws exception
        }
    } catch(Exception e){
    
    } 
    
```    
- Marking, resetting and Skipping positions
    - All readers support skipping elements
    - reset, may be supported by some, but there is no way to test it.
    - marking, may be supported,but can be checked by markSupported()
    - mark() - keeps a flag on the given element
    - reset() - rewind to the previously marked one
    - skip() - skip the next element
    
    
- Creating a reader
    - Based on type of input
        - Disk
            - FileReader
            ```java
                  File file = new File("files/data.txt");
                  Reader reader = new FileReader(file);
            ``` 
        - In-memory
            - CharArrayReader
            - StringReader
            ```java
                  String text = "Hello World!";
                  Reader reader = new StringReader(text);
            ```             
    - Based on beahvior - Follow GoF Decorator Pattern
        - BufferedReader
            - Java 1 way (File contents must be UTF-8)
            ```java
                  File file = new File("files/data.txt");
                  Reader fileReader = new FileReader(file);
                  BufferedReader bufferedReader = new BufferedReader(fileReader);
                  //NOTE: reading from fileReader and bufferedReader are reading the same file.
                  // filReader.read() followed by bufferedReader.read(), reads the next char
                  // same with the close(). No need to close both.
            ``` 
                
            - Java 7 way (File contents must be UTF-8)
            ```java
                  Path path = Paths.get("files/data.txt");
                  BufferedReader bufferedReader = Files.newBufferedReader(path);
          
                  BufferedReader bufferedReaderWithCharset = Files.newBufferedReader(path, StandardCharset.ISO_8859_1);
                  //Note: this is built on top of InputStreamReader class
            ```           
            - adds readLine()
        - LineNumberReader
        ```java
              File file = new File("files/data.txt");
              Reader fileReader = new FileReader(file);
              BufferedReader bufferedReader = new BufferedReader(fileReader);
        ``` 
            - Extends BufferedReader, hence it has readLine()
            - adds getLineNumber()
        - PushbackReader


Reader
    - close()
    - read(char[], int, int)
    
    - mark(int)
    - markSupported()
    - read()
    - read(char[])
    - read(CharBuffer)
    - ready()
    - reset()
    - skip(long)

FileReader extends Reader
    - File
    - close()
    - read(char[], int, int)
CharArrayReader extends Reader
    - char[]
    - close()
    - read(char[], int, int)
    
BufferedReader extends Reader
    - fileReader
    - charReader
    - readLine()

Writers

Writing Characters into Files

Writing Characters into In-Memory



Append a single character or a string
    -   Write a single character or string
        ```java
            Writer writer = ...
            writer.write('H');
            writer.write("Hello World!");
            writer.write("Hello World!",0,5); //Writes Hello  
            String hello = "Hello World!";
            writer.write(hello.toCharArray(),0,5)
            //Note: write() does not return any value
        ```
    -   Handling Exceptions
        ```java
            try(Writer writer = ...){
                writer.write("Hello World!");
            } catch (IOException e) {
                //Handle exception
            }  
        ```
    - Creating Writers
        - Type
            - Disk
                - FileWriter - Writes to file
                        ```java
                            File file = new File("files/data.txt);
                            try(Writer writer = new FileWriter(file);){ //new FileWriter(file,true) will append to existing file
                                writer.write("Hello World!");
                            } catch (IOException e) {
                                //Handle exception
                            }  
                        ```
            - In-memory
                - CharArrayWriter
                - StringWriter - Writes to a StringBuffer
        - Behavior
            - BufferedWriter
                ```java
                    File file = new File("files/data.txt);
                    try(Writer writer = new FileWriter(file);
                        BufferedWriter bw = new BufferedWriter(writer)){ 
                        bw.write("Hello World!");
                    } catch (IOException e) {
                        //Handle exception
                    }  
                ```
                - By default uses UTF-8 charset
                - Java 7 way
                                ```java
                                    Path path = Paths.get("files/data.txt);
                                    try(BufferedWriter bw = Files.newBufferedWriter(path)){  
                                    //To specifiy a charset you can use Files.newBufferedWriter(path, StandardCharsets.ISO_8859_1))
                                        bw.write("Hello World!");
                                    } catch (IOException e) {
                                        //Handle exception
                                    }  
                                ```
                - To specifiy a charset you can use Files.newBufferedWriter(path, StandardCharsets.ISO_8859_1))
                - To specifiy OpenOption you can use Files.newBufferedWriter(path, StandardOpenOption.CREATE))
                - Various OpenOption, 
                    - WRITE,APPEND
                    - CREATE, CREATE_NEW
                    - DELETE_ON_CLOSE - To create temp files
            - PrintWriter
                ```java
                    File file = new File("files/data.txt);
                    try(Writer writer = new FileWriter(file);
                        PrintWriter pw= new PrintWriter(writer)){ 
                        pw.write("Hello World!");
                    } catch (IOException e) {
                        //Handle exception
                    }  
                ```
                - Syntax same as sprintf of Unix
                                    
Writer
    close()
    flush()
    write(char[], int, int)
    
    append(char)
    append(CharSequence)
    append(CharSequence, int,  int)
    write(char[])
    write(int)
    write(String)
    write(String,int,int)
    
FileWriter extends Writer
    Uses
        File
    implements
        close()
        flush()
        write(char[], int, int)
        
StringWriter extends Writer
    Uses
        StringBuffer
    implements
        close()
        flush()
        write(char[], int, int)
        
BufferedWriter extends Writer
    Uses
        Another Writer using composition relationship, i.e specified during object creation
        It could be a FileWriter, StringWriter or any other writer
    New method added
        newLine()
    
flush() - > triggers System call