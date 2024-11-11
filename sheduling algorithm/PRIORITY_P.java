import java.util.Scanner;

public class PRIORITY_P {

    // Class to represent each process
    static class Process {
        int id;              // Process ID
        int burst;           // Burst time
        int priority;        // Priority (lower value = higher priority)
        int arrival;         // Arrival time
        int waitingTime;     // Waiting time (calculated)
        int turnaroundTime;  // Turnaround time (calculated)
        int remainingBurst;  // Remaining burst time

        // Constructor to initialize process attributes
        public Process(int id, int burst, int priority, int arrival) {
            this.id = id;
            this.burst = burst;
            this.priority = priority;
            this.arrival = arrival;
            this.remainingBurst = burst; // Initially, remaining burst is equal to burst time
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input: Number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        Process[] processes = new Process[n];

        // Input: Arrival time, burst time, and priority for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time, burst time, and priority for process " + (i + 1) + ": ");
            int at = sc.nextInt();  // Arrival time
            int bt = sc.nextInt();  // Burst time
            int priority = sc.nextInt();  // Priority
            processes[i] = new Process(i + 1, bt, priority, at);  // Create new process
        }

        int completed = 0; // Number of processes completed
        int time = 0; // Current time
        String ganttChart = ""; // To build the Gantt Chart

        // Process scheduling until all processes are completed
        while (completed < n) {
            int idx = -1; // Index of the process to execute
            int highestPriority = Integer.MAX_VALUE; // Highest priority (lower value)

            // Find the process with the highest priority that is ready
            for (int i = 0; i < n; i++) {
                if (processes[i].arrival <= time && processes[i].remainingBurst > 0 && processes[i].priority < highestPriority) {
                    highestPriority = processes[i].priority;
                    idx = i;
                }
            }

            if (idx != -1) { // If a process is selected
                processes[idx].remainingBurst--; // Decrement the remaining burst time
                ganttChart += "P" + processes[idx].id + " "; // Append to Gantt Chart

                if (processes[idx].remainingBurst == 0) { // If the process is completed
                    completed++; // Increment completed process count
                    processes[idx].turnaroundTime = time + 1 - processes[idx].arrival; // Turnaround Time
                    processes[idx].waitingTime = processes[idx].turnaroundTime - processes[idx].burst; // Waiting Time
                }
            } else {
                // If no process is ready, CPU is idle
                ganttChart += "idle ";
            }

            time++; // Increment time
        }

        // Print the Gantt Chart
        System.out.println("Gantt Chart: " + ganttChart);

        // Calculate and display the process details
        int totalWT = 0;  // Total waiting time
        int totalTAT = 0; // Total turnaround time

        System.out.println("Process\tArrival\tBurst\tPriority\tWaiting\tTurnaround");
        for (Process p : processes) {
            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
            System.out.println("P" + p.id + "\t" + p.arrival + "\t" + p.burst + "\t" + p.priority + "\t\t" + p.waitingTime + "\t" + p.turnaroundTime);
        }

        // Calculate and print average waiting time and average turnaround time
        System.out.println("Average Waiting Time: " + (totalWT / (float) n));
        System.out.println("Average Turnaround Time: " + (totalTAT / (float) n));

        sc.close(); // Close the scanner
    }
}
