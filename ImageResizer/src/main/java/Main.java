
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;



public class Main {
    private static int newWidth = 300;
    private static int CORES = Runtime.getRuntime().availableProcessors();


    public static void main(String[] args) {
        String srcFolder = "images";
        String dstFolder = "reducedImages";


        File srcDir = new File(srcFolder);
        long start = System.currentTimeMillis();
        File[] files = srcDir.listFiles();

        System.out.println("Количество ядер" + CORES);

        ConcurrentLinkedQueue<File> queue = new ConcurrentLinkedQueue<>();
        for (File file : files) {
            queue.add(file);
        }

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < CORES; i++) {
            ConcurrentLinkedQueue<File> linkedQueue = nin(queue);
            ImageResizer imageResizer = new ImageResizer(linkedQueue, newWidth, dstFolder, start);
            Thread thread = new Thread(imageResizer);
            threads.add(thread);
            for (File l : linkedQueue) {
                linkedQueue.remove(l);
                queue.remove(l);
            }
        }
        threads.forEach(Thread::start);
    }

    private static ConcurrentLinkedQueue<File> nin(ConcurrentLinkedQueue<File> files) {
        ConcurrentLinkedQueue<File> linkedQueue = files;
        int size = linkedQueue.size() / CORES;
        ConcurrentLinkedQueue<File> s = new ConcurrentLinkedQueue<>();
        for (File file : linkedQueue) {
            if (s.size() <= size) {
                s.add(file);
            }
        }
        return s;
    }
}




