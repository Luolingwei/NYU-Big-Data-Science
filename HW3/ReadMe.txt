Lingwei Luo(lingweiluo@nyu.edu)

Sumitted Documents:

	1. Source code: All .java files under /Source Code/src/main/java/, they are submitted for your reference, Not for running the program. 

	2. "outputFiles": all program outputs are stored in this folder (.txt).

	3. HW3.pdf: all written answers and model outputs.

	4. HW3.jar: it is the executable file for program running.

How to run my program: 

	1. Log into Prince:
		$ ssh hpcgwtunnel
		$ ssh -Y prince

	2. request an interactive shell by using the following command:
		$ srun --mem=10GB --time=01:00:00 --cpus-per-task 1 --pty $SHELL
		Note: if the job is killed, please try request more "mem" and "time".

	3. run the executable file HW3.jar
		$ java -jar HW3.jar

	When the program finishes, all program outputs will appear in your current path.
	Note: if there is warnings like "SLF4JL Failed to load class....", just ignore it, the program will execute correctly. As Stanford NLP lib is used in my program, it will take very long time to execute it, it took me around 4 hours to run it on HPC.