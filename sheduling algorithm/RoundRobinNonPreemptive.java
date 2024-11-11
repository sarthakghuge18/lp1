import java.util.Scanner;

public class RoundRobinNonPreemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input: Number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        // Arrays to store burst time, remaining time, waiting time, and turnaround time
        int[] burstTime = new int[n];
        int[] remainingTime = new int[n];
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];

        // Input: Time quantum
        System.out.print("Enter time quantum: ");
        int quantum = sc.nextInt();

        // Input: Burst time for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            burstTime[i] = sc.nextInt();         // Store burst time
            remainingTime[i] = burstTime[i];     // Initialize remaining time as burst time
        }

        int time = 0;       // Current time in the system
        int completed = 0;  // Counter for completed processes
        String ganttChart = ""; // Gantt chart string to record the execution order

        // Scheduling loop: Runs until all processes are completed
        while (completed < n) {
            boolean processExecuted = false; // Flag to check if any process was executed

            // Iterate over all processes
            for (int i = 0; i < n; i++) {
                if (remainingTime[i] > 0) { // If the process still has remaining burst time
                    processExecuted = true;

                    // If the process can be fully executed within the quantum
                    if (remainingTime[i] > quantum) {
                        time += quantum;         // Advance time by the quantum
                        remainingTime[i] -= quantum; // Reduce remaining burst time
                        ganttChart += "P" + (i + 1) + " "; // Add process to Gantt chart
                    } else {
                        // If the remaining burst time is less than or equal to the quantum
                        time += remainingTime[i];  // Advance time by remaining burst time
                        remainingTime[i] = 0;      // Mark process as completed
                        ganttChart += "P" + (i + 1) + " "; // Add process to Gantt chart

                        completed++; // Increment the completed process count
                        // Calculate waiting time and turnaround time
                        waitingTime[i] = time - burstTime[i]; // Waiting Time = Total Time - Burst Time
                        turnaroundTime[i] = time;            // Turnaround Time = Total Time
                    }
                }
            }

            // If no process was executed, CPU remains idle
            if (!processExecuted) {
                ganttChart += "idle ";
                time++; // Advance time by 1 unit
            }
        }

        // Output: Gantt Chart
        System.out.println("Gantt Chart: " + ganttChart);

        // Calculate total waiting time and turnaround time
        int totalWT = 0;  // Total waiting time
        int totalTAT = 0; // Total turnaround time

        // Output: Process details (burst time, waiting time, turnaround time)
        System.out.println("Process\tBurst\tWaiting\tTurnaround");
        for (int i = 0; i < n; i++) {
            totalWT += waitingTime[i]; // Sum of waiting times
            totalTAT += turnaroundTime[i]; // Sum of turnaround times
            System.out.println("P" + (i + 1) + "\t" + burstTime[i] + "\t" + waitingTime[i] + "\t" + turnaroundTime[i]);
        }

        // Output: Average waiting time and turnaround time
        System.out.println("Average Waiting Time: " + (totalWT / (float) n));
        System.out.println("Average Turnaround Time: " + (totalTAT / (float) n));

        sc.close(); // Close scanner
    }
}

