import java.util.Scanner;

// Class to represent a process
class Process {
    int pid; // Process ID
    int burstTime; // Burst time of the process
    int arrivalTime; // Arrival time of the process
    int waitingTime; // Waiting time (to be calculated)
    int turnaroundTime; // Turnaround time (to be calculated)

    // Constructor to initialize the process attributes
    public Process(int pid, int burstTime, int arrivalTime) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
    }
}

public class FCFS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input: Number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        Process[] processes = new Process[n]; // Array to store process objects

        // Input: Arrival time and burst time for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time and burst time for process " + (i + 1) + ": ");
            int at = sc.nextInt(); // Arrival time
            int bt = sc.nextInt(); // Burst time
            processes[i] = new Process(i + 1, bt, at); // Create a new Process object
        }

        // Sort processes based on their arrival time using Bubble Sort
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (processes[j].arrivalTime > processes[j + 1].arrivalTime) {
                    // Swap processes if the current process has a later arrival time
                    Process temp = processes[j];
                    processes[j] = processes[j + 1];
                    processes[j + 1] = temp;
                }
            }
        }

        // Variables to calculate total waiting time and total turnaround time
        int totalTime = 0; // Tracks the current time in the schedule
        int totalWT = 0; // Total waiting time
        int totalTAT = 0; // Total turnaround time

        // Calculate waiting time and turnaround time for each process
        for (Process p : processes) {
            p.waitingTime = totalTime - p.arrivalTime; // Calculate waiting time
            totalTime += p.burstTime; // Update the current time after processing
            p.turnaroundTime = p.waitingTime + p.burstTime; // Calculate turnaround time
            totalWT += p.waitingTime; // Add to total waiting time
            totalTAT += p.turnaroundTime; // Add to total turnaround time
        }

        // Print the Gantt Chart
        System.out.println("Gantt Chart: ");
        for (Process p : processes) {
            System.out.print("P" + p.pid + " ");
        }
        System.out.println("\n");

        // Print process details
        System.out.println("Process\tArrival\tBurst\tWaiting\tTurnaround");
        for (Process p : processes) {
            System.out.println("P" + p.pid + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" + p.waitingTime + "\t" + p.turnaroundTime);
        }

        // Print average waiting time and average turnaround time
        System.out.println("Average Waiting Time: " + (totalWT / (float) n));
        System.out.println("Average Turnaround Time: " + (totalTAT / (float) n));

        sc.close(); // Close the scanner
    }
}
