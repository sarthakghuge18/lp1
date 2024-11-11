import java.util.*;

public class Fifo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input: Number of frames
        System.out.print("Enter the number of frames: ");
        int numberOfFrames = scanner.nextInt();

        // Input: Number of pages in the reference string
        System.out.print("Enter the number of pages: ");
        int numberOfPages = scanner.nextInt();

        // Input: Page reference string
        System.out.print("Enter the page reference string (space-separated): ");
        int[] pageReferenceString = new int[numberOfPages];
        for (int i = 0; i < numberOfPages; i++) {
            pageReferenceString[i] = scanner.nextInt();
        }

        // Initialize frames array to hold the pages in memory (-1 indicates empty)
        int[] frames = new int[numberOfFrames];
        Arrays.fill(frames, -1); // Fill frames with -1 to signify they're empty

        int pageFaults = 0; // Counter for page faults
        int currentIndex = 0; // Tracks the oldest frame to replace (FIFO logic)

        // Process each page in the reference string
        for (int page : pageReferenceString) {
            boolean pageHit = false;

            // Check if the page is already in the frames (page hit)
            for (int frame : frames) {
                if (frame == page) {
                    pageHit = true; // Page is already in memory, no fault
                    break;
                }
            }

            // If the page is not found in the frames (page fault)
            if (!pageHit) {
                // Replace the oldest page with the new page using FIFO logic
                frames[currentIndex] = page;

                // Update the index of the next frame to be replaced (cyclically)
                currentIndex = (currentIndex + 1) % numberOfFrames;

                pageFaults++; // Increment the page fault count
            }

            // Print the current state of frames after processing the page
            System.out.print("Frames: ");
            for (int frame : frames) {
                System.out.print(frame + " ");
            }
            System.out.println();
        }

        // Output total page faults and the fault ratio
        System.out.println("Total Page Faults: " + pageFaults);
        System.out.println("Page Faults ratio: " + pageFaults + ":" + numberOfPages);

        scanner.close(); // Close the scanner
    }
}
