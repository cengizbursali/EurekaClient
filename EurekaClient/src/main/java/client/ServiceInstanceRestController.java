package client;

import org.springframework.web.bind.annotation.*;
import service.DatabaseService;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static util.Constants.ORDER_ASCENDING;

@RestController
class ServiceInstanceRestController {

    private DatabaseService databaseService;

    @PostConstruct
    public void init() {
        databaseService = new DatabaseService();
    }

    @RequestMapping("/list")
    public String serviceInstancesByApplicationName(@RequestParam("order") Optional<String> order) {
        return order.map(s -> databaseService.list(s)).orElseGet(() -> databaseService.list(ORDER_ASCENDING));
    }

    @RequestMapping("/number/max")
    public String max() {
        return databaseService.getMax();
    }

    @RequestMapping("/number/min")
    public String min() {
        return databaseService.getMin();
    }

    @RequestMapping("/number/{number}")
    public String get(@PathVariable Integer number) {
        return databaseService.get(number);
    }

    @RequestMapping("/delete/{number}")
    public String delete(@PathVariable Integer number) {
        return databaseService.delete(number);
    }

    @RequestMapping(value = "/insert/{number}", method = RequestMethod.POST)
    public String insert(@PathVariable Integer number) {
        return databaseService.insert(number);
    }
}