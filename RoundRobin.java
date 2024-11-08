import java.util.Scanner;

public class RoundRobin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input: Number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        // Arrays to store arrival time, burst time, remaining time, waiting time, and turnaround time
        int[] burstTime = new int[n];
        int[] remainingTime = new int[n];
        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        int[] arrivalTime = new int[n];

        // Input: Time quantum
        System.out.print("Enter time quantum: ");
        int quantum = sc.nextInt();

        // Input: Arrival time and burst time for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time and burst time for process " + (i + 1) + ": ");
            arrivalTime[i] = sc.nextInt();  // Store arrival time
            burstTime[i] = sc.nextInt();   // Store burst time
            remainingTime[i] = burstTime[i]; // Initialize remaining time as burst time
        }

        int currentTime = 0;        // Keeps track of the current time
        int completedProcesses = 0; // Counter for completed processes
        String ganttChart = "";      // String to represent the Gantt chart

        // Scheduling loop: Runs until all processes are completed
        while (completedProcesses < n) {
            boolean found = false; // Flag to check if any process was executed in this cycle

            // Iterate over all processes
            for (int i = 0; i < n; i++) {
                // Check if the process is ready to execute (arrived) and has remaining burst time
                if (remainingTime[i] > 0 && arrivalTime[i] <= currentTime) {
                    found = true;

                    // If the remaining burst time is greater than the quantum
                    if (remainingTime[i] > quantum) {
                        currentTime += quantum;        // Advance time by the quantum
                        remainingTime[i] -= quantum;   // Reduce remaining burst time
                        ganttChart += "P" + (i + 1) + " "; // Add process to Gantt chart
                    } else {
                        // If the process can complete in the current cycle
                        currentTime += remainingTime[i]; // Advance time by the remaining burst time
                        ganttChart += "P" + (i + 1) + " "; // Add process to Gantt chart

                        // Calculate waiting and turnaround times
                        waitingTime[i] = currentTime - burstTime[i] - arrivalTime[i];
                        turnaroundTime[i] = waitingTime[i] + burstTime[i];
                        remainingTime[i] = 0; // Mark process as completed
                        completedProcesses++; // Increment completed process count
                    }
                }
            }

            // If no process is ready, the CPU is idle
            if (!found) {
                currentTime++; // Advance time by 1 unit
                ganttChart += "idle ";
            }
        }

        // Output: Gantt Chart
        System.out.println("Gantt Chart: " + ganttChart);

        // Calculate and display waiting and turnaround times for each process
        float totalWT = 0, totalTAT = 0; // Variables to store total waiting and turnaround times
        System.out.println("Process\tArrival\tBurst\tWaiting\tTurnaround");
        for (int i = 0; i < n; i++) {
            totalWT += waitingTime[i]; // Sum of waiting times
            totalTAT += turnaroundTime[i]; // Sum of turnaround times
            System.out.println("P" + (i + 1) + "\t" + arrivalTime[i] + "\t" + burstTime[i] + "\t" + waitingTime[i] + "\t" + turnaroundTime[i]);
        }

        // Output: Average waiting and turnaround times
        System.out.println("Average Waiting Time: " + (totalWT / n));
        System.out.println("Average Turnaround Time: " + (totalTAT / n));

        sc.close(); // Close scanner
    }
}
