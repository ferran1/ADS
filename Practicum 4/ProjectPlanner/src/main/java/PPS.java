import utils.Calendar;
import utils.SLF4J;
import utils.XMLParser;
import utils.XMLWriter;

import javax.xml.stream.XMLStreamConstants;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PPS {

    private static Random randomizer = new Random();

    private String name;                // the name of the planning system refers to its xml source file
    private int planningYear;                   // the year indicates the period of start and end dates of the projects
    private Set<Employee> employees;
    private Set<Project> projects;

    @Override
    public String toString() {
        return String.format("PPS_e%d_p%d", this.employees.size(), this.projects.size());
    }

    private PPS() {
        this.name = "none";
        this.planningYear = 2000;
        this.projects = new TreeSet<>();
        this.employees = new TreeSet<>();
    }

    private PPS(String resourceName, int year) {
        this();
        this.name = resourceName;
        this.planningYear = year;
    }

    /**
     * Reports the statistics of the project planning year
     */
    public void printPlanningStatistics() {
        System.out.printf("\nProject Statistics of '%s' in the year %d\n",
                this.name, this.planningYear);
        if (this.employees == null || this.projects == null ||
                this.employees.size() == 0 || this.projects.size() == 0) {
            System.out.println("No employees or projects have been set up...");
            return;
        }

        System.out.printf("%d employees have been assigned to %d projects:\n\n",
                this.employees.size(), this.projects.size());

        // TODO calculate and display statistics
        System.out.println("1. The average hourly wage of all employees is " + calculateAverageHourlyWage());
        System.out.println("2. The longest project is '" + calculateLongestProject() + "' with " + calculateLongestProject().getNumWorkingDays() + " available working days.");
        System.out.println("3. The following employees have the broadest assignment in no less than 10 different projects: \n" + calculateMostInvolvedEmployees());
        System.out.println("4. The total budget of committed project manpower is " + calculateTotalManpowerBudget());
        System.out.println("5. Below is an overview of total managed budget by junior employees (hourly wage <= 30): \n" + calculateManagedBudgetOverview((e) -> e.getHourlyWage() <= 30));
        System.out.println("6. Below is an overview of cumulative monthly project spends: \n" + calculateCumulativeMonthlySpends());
    }

    /**
     * calculates the average hourly wage of all known employees in this system
     *
     * @return
     */
    public double calculateAverageHourlyWage() {
        // loop through each employee and get its hourly wage and sum these wages
        // after get the average of all the summed wages
        return this.getEmployees().stream()
                .mapToInt(Employee::getHourlyWage)
                .average().getAsDouble();
    }

    /**
     * finds the project with the highest number of available working days.
     * (if more than one project with the highest number is found, any one is returned)
     *
     * @return
     */
    public Project calculateLongestProject() {
        return getProjects().stream()
                .max(Comparator.comparing(Project::getNumWorkingDays))
                .get();
    }

    /**
     * calculates the total budget for assigned employees across all projects and employees in the system
     * based on the registration of committed hours per day per employee,
     * the number of working days in each project
     * and the hourly rate of each employee
     *
     * @return
     */
    public int calculateTotalManpowerBudget() {
        return this.getProjects().stream()
                .mapToInt(Project::calculateManpowerBudget)
                .reduce(0, Integer::sum);
    }

    /**
     * finds the employees that are assigned to the highest number of different projects
     * (if multiple employees are assigned to the same highest number of projects,
     * all these employees are returned in the set)
     *
     * @return
     */
    public Set<Employee> calculateMostInvolvedEmployees() {

        // First get the maxSize with the highest number of assigned projects
        int maxSize = getEmployees().stream()
                .mapToInt(employee -> employee.getAssignedProjects().size())
                .reduce(0, (i, i1) -> Math.max(i1, i));
        // Return the set with all employees that have the number of assigned projects equal to 'maxSize'
        return getEmployees().stream()
                .filter(employee -> employee.getAssignedProjects().size() >= maxSize)
                .collect(Collectors.toSet());
    }

    /**
     * Calculates an overview of total managed budget per employee that complies with the filter predicate
     * The total managed budget of an employee is the sum of all man power budgets of all projects
     * that are being managed by this employee
     *
     * @param filter
     * @return
     */
    public Map<Employee, Integer> calculateManagedBudgetOverview(Predicate<Employee> filter) {
        return getEmployees().stream()
                .filter(filter)
                .collect(Collectors.toMap(
                        employee -> employee,
                        Employee::calculateManagedBudget,
                        Math::addExact
                ));
    }

    /**
     * Calculates an overview of total monthly spends across all projects in the system
     * The monthly spend of a single project is the accumulated manpower cost of all employees assigned to the
     * project across all working days in the month.
     *
     * @return
     */
    public Map<Month,Integer> calculateCumulativeMonthlySpends() {
        Map<Month, Integer> totalMonthlySpendsOverview = new TreeMap<>();

        for (Project project: projects){
            //Loop through each day, get the month and put the value in the correct month (key)
            for (LocalDate localDate: project.getWorkingDays()){
                // Check if the month already exists in the HashMap, if so update the old value
                if (totalMonthlySpendsOverview.containsKey(localDate.getMonth())){
                    totalMonthlySpendsOverview.merge(localDate.getMonth(), project.calculateManpowerBudget() / project.getNumWorkingDays(), Math::addExact);
                } else {
                    totalMonthlySpendsOverview.put(localDate.getMonth(), project.calculateManpowerBudget() / project.getNumWorkingDays());
                }
            }
        }
        return totalMonthlySpendsOverview;
    }

    public String getName() {
        return name;
    }

    /**
     * A builder helper class to compose a small PPS using method-chaining of builder methods
     */
    public static class Builder {
        PPS pps;

        public Builder() {
            this.pps = new PPS();
        }

        /**
         * Add another employee to the PPS being build
         *
         * @param employee
         * @return
         */
        public Builder addEmployee(Employee employee) {
            this.pps.getEmployees().add(employee);
            return this;
        }

        /**
         * Add another project to the PPS
         * register the specified manager as the manager of the new
         *
         * @param project
         * @param manager
         * @return
         */
        public Builder addProject(Project project, Employee manager) {
            boolean isAlreadyEmployee = this.pps.getEmployees().contains(manager);
            if (isAlreadyEmployee) {
                Employee finalManager = manager;
                manager = this.pps.getEmployees().stream()
                        .filter(employee -> employee.equals(finalManager))
                        .findFirst().orElse(null);
            }

            manager.getManagedProjects().add(project);
            this.pps.getProjects().add(project);
            this.pps.getEmployees().add(manager);
            return this;
        }

        /**
         * Add a commitment to work hoursPerDay on the project that is identified by projectCode
         * for the employee who is identified by employeeNr
         * This commitment is added to any other commitment that the same employee already
         * has got registered on the same project,
         *
         * @param projectCode
         * @param employeeNr
         * @param hoursPerDay
         * @return
         */
        public Builder addCommitment(String projectCode, int employeeNr, int hoursPerDay) {
            Project project = this.pps.getProjects().stream().filter(project1 -> project1.getCode().equals(projectCode)).findFirst().orElse(null);
            Employee employee = this.pps.getEmployees().stream().filter(employee1 -> employee1.getNumber() == employeeNr).findFirst().orElse(null);

            if (employee == null) {
                employee = new Employee(employeeNr);
                this.addEmployee(employee);
            }
            if (project != null) {
                project.addCommitment(employee, hoursPerDay);
            }
//                    .forEach(s -> {
//                s.getCommittedHoursPerDay().entrySet().stream()
//                        .filter(employeeIntegerEntry -> employeeIntegerEntry.getKey().getNumber() == employeeNr)
//                        .forEach(employeeIntegerEntry -> );
//            });
            return this;
        }

        /**
         * Complete the PPS being build
         *
         * @return
         */
        public PPS build() {
            return this.pps;
        }
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    /**
     * Loads a complete configuration from an XML file
     *
     * @param resourceName the XML file name to be found in the resources folder
     * @return
     */
    public static PPS importFromXML(String resourceName) {
        XMLParser xmlParser = new XMLParser(resourceName);

        try {
            xmlParser.nextTag();
            xmlParser.require(XMLStreamConstants.START_ELEMENT, null, "projectPlanning");
            int year = xmlParser.getIntegerAttributeValue(null, "year", 2000);
            xmlParser.nextTag();

            PPS pps = new PPS(resourceName, year);

            Project.importProjectsFromXML(xmlParser, pps.projects);
            Employee.importEmployeesFromXML(xmlParser, pps.employees, pps.projects);

            return pps;

        } catch (Exception ex) {
            SLF4J.logException("XML error in '" + resourceName + "'", ex);
        }

        return null;
    }
}
