import java.util.Scanner;

public class SJFPreemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input: Number of processes
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        int[] bt = new int[n];        // Burst Time
        int[] at = new int[n];        // Arrival Time
        int[] rt = new int[n];        // Remaining Time
        int[] wt = new int[n];        // Waiting Time
        int[] tat = new int[n];       // Turnaround Time
        boolean[] completed = new boolean[n]; // Track completed processes

        // Input: Arrival Time and Burst Time for each process
        System.out.println("Enter Arrival Time and Burst Time of the processes:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + (i + 1) + ": ");
            at[i] = sc.nextInt();  // Arrival Time
            bt[i] = sc.nextInt();  // Burst Time
            rt[i] = bt[i];         // Initialize Remaining Time with Burst Time
        }

        int completedProcesses = 0;   // Number of completed processes
        int currentTime = 0;          // Current time in the Gantt Chart
        int shortest = 0;             // Index of the process with the shortest remaining time
        boolean found;                // Flag to check if a suitable process is found
        String ganttChart = "";       // Gantt Chart representation

        // Process execution loop until all processes are completed
        while (completedProcesses < n) {
            found = false; // Reset found flag for each time unit

            // Find the shortest job available at the current time
            for (int i = 0; i < n; i++) {
                if (!completed[i] && at[i] <= currentTime && (found == false || rt[i] < rt[shortest])) {
                    shortest = i;
                    found = true;
                }
            }

            // Execute the shortest process or wait if no process is ready
            if (found) {
                rt[shortest]--; // Reduce the remaining time of the selected process
                ganttChart += "P" + (shortest + 1) + " "; // Add to Gantt Chart
                currentTime++; // Advance the current time

                // If the process is completed
                if (rt[shortest] == 0) {
                    completed[shortest] = true; // Mark process as completed
                    completedProcesses++; // Increment the completed count
                    tat[shortest] = currentTime - at[shortest]; // Calculate Turnaround Time
                    wt[shortest] = tat[shortest] - bt[shortest]; // Calculate Waiting Time
                }
            } else {
                // If no process is available, CPU remains idle
                currentTime++;
                ganttChart += "idle ";
            }
        }

        // Output Gantt Chart
        System.out.println("Gantt Chart: " + ganttChart);

        // Calculate and display average waiting and turnaround times
        float avgWT = 0, avgTAT = 0;
        System.out.println("Process\tArrival\tBurst\tWaiting\tTurnaround");
        for (int i = 0; i < n; i++) {
            avgWT += wt[i];
            avgTAT += tat[i];
            System.out.println("P" + (i + 1) + "\t" + at[i] + "\t" + bt[i] + "\t" + wt[i] + "\t" + tat[i]);
        }

        avgWT /= n; // Calculate average waiting time
        avgTAT /= n; // Calculate average turnaround time

        // Display average times
        System.out.println("Average Waiting Time: " + avgWT);
        System.out.println("Average Turnaround Time: " + avgTAT);

        sc.close(); // Close the scanner
    }
}
