import java.util.*;

public class Lru {
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

        // Use a LinkedList to represent the frames (ordered by recency of use)
        LinkedList<Integer> frames = new LinkedList<>();

        int pageFaults = 0; // Counter for page faults

        // Process each page in the reference string
        for (int page : pageReferenceString) {
            // If the page is not in the frames (page fault)
            if (!frames.contains(page)) {
                // If the frames are full, remove the least recently used (first) page
                if (frames.size() >= numberOfFrames) {
                    frames.removeFirst();
                }
                // Add the current page to the end (most recently used)
                frames.addLast(page);
                pageFaults++; // Increment the page fault count
            } else {
                // If the page is already in the frames, move it to the end (update recency)
                frames.remove(frames.indexOf(page));
                frames.addLast(page);
            }

            // Print the current state of frames
            System.out.print("Frames: ");
            for (int frame : frames) {
                System.out.print(frame + " ");
            }
            System.out.println();
        }

        // Output total page faults and the fault ratio
        System.out.println("Total Page Faults: " + pageFaults);
        System.out.println("Page Fault ratio: " + pageFaults + ":" + numberOfPages);

        scanner.close(); // Close the scanner
    }
}
