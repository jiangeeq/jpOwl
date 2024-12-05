@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@Service
public class DemoService {
    @Autowired
    private JpOwl jpOwl;
    
    @Monitor(logLevel = LogLevel.INFO)
    public void businessMethod() {
        // 业务逻辑
    }
} 