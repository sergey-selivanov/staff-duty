rem \ escapes inner double quotes for --args
rem ^" escapes for cmd for multiline string

gradlew run --args=^"^
-config \^"c:\tmp\staff duty\sample.properties\^" ^
-file \^"c:\tmp\staff duty\sample.xlsx\^" ^
"