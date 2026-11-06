import java.awt.*
import java.nio.file.*
import java.time.LocalDate
import java.util.Properties
import javax.swing.*

data class Habit(val name: String, val days: MutableSet<LocalDate> = sortedSetOf())
fun streak(habit: Habit, today: LocalDate): Int {
    var day = if (today in habit.days) today else today.minusDays(1); var count = 0
    while (day in habit.days) { count++; day = day.minusDays(1) }; return count
}
fun stage(habit: Habit) = when { habit.days.size >= 30 -> "Flourishing tree"; habit.days.size >= 14 -> "Young tree"; habit.days.size >= 7 -> "Flowering plant"; habit.days.size >= 3 -> "Sprout"; else -> "Seedling" }
fun save(path: Path, habits: List<Habit>) {
    require(habits.size <= 100 && habits.map { it.name.lowercase() }.distinct().size == habits.size && habits.all { it.name.isNotBlank() && it.name.length <= 80 })
    val p = Properties(); p.setProperty("count", habits.size.toString())
    habits.forEachIndexed { i, h -> p.setProperty("habit.$i.name", h.name); p.setProperty("habit.$i.days", h.days.sorted().joinToString(",")) }
    val full = path.toAbsolutePath(); Files.createDirectories(full.parent); val tmp = Files.createTempFile(full.parent, "garden-", ".tmp")
    try { Files.newBufferedWriter(tmp).use { p.store(it, "Habit Garden") }; try { Files.move(tmp, full, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING) } catch (e: AtomicMoveNotSupportedException) { Files.move(tmp, full, StandardCopyOption.REPLACE_EXISTING) } } finally { Files.deleteIfExists(tmp) }
}
fun load(path: Path): MutableList<Habit> {
    val p = Properties(); Files.newBufferedReader(path).use { p.load(it) }; val count = p.getProperty("count").toInt(); require(count in 0..100)
    val result = (0 until count).map { i -> Habit(p.getProperty("habit.$i.name") ?: error("Missing name"), p.getProperty("habit.$i.days", "").split(',').filter { it.isNotBlank() }.map { LocalDate.parse(it) }.toSortedSet()) }.toMutableList()
    require(result.all { it.name.isNotBlank() && it.name.length <= 80 } && result.map { it.name.lowercase() }.distinct().size == result.size); return result
}
class Garden(private val habits: () -> List<Habit>, private val selected: () -> Int) : JPanel() {
    init { preferredSize = Dimension(600, 270); background = Color(234, 245, 235) }
    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics); val g = graphics as Graphics2D; g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.color = Color(133, 181, 108); g.fillRect(0, height - 35, width, 35)
        val entries = habits().take(10)
        entries.forEachIndexed { i, h ->
            val x = (i + 1) * width / (entries.size + 1); val growth = minOf(150, 15 + h.days.size * 5)
            g.color = Color(86, 115, 66); g.fillRect(x - 3, height - 35 - growth, 6, growth)
            g.color = if (i == selected()) Color(24, 133, 104) else Color(72, 161, 94)
            val radius = minOf(30, 9 + h.days.size); g.fillOval(x - radius, height - 35 - growth - radius, radius * 2, radius * 2)
            g.color = Color(49, 70, 57); g.drawString(h.name.take(10), x - 25, height - 12)
        }
    }
}
fun main(args: Array<String>) {
    if (args.contains("--self-test")) {
        val today = LocalDate.of(2030, 1, 10); val h = Habit("Read", sortedSetOf(today.minusDays(2), today.minusDays(1))); check(streak(h, today) == 2); h.days += today; check(streak(h, today) == 3); check(streak(h, today.plusDays(2)) == 0); check(stage(h) == "Sprout")
        val dir = Files.createTempDirectory("garden-test"); val path = dir.resolve("garden.properties"); try { save(path, listOf(h)); check(load(path) == listOf(h)) } finally { Files.deleteIfExists(path); Files.deleteIfExists(dir) }; println("Streak, growth stage, and persistence passed"); return
    }
    val path = Path.of(args.firstOrNull() ?: "garden.properties")
    SwingUtilities.invokeLater {
        var habits = mutableListOf<Habit>(); try { if (Files.exists(path)) habits = load(path) } catch (e: Exception) { JOptionPane.showMessageDialog(null, "Cannot load garden: ${e.message}"); return@invokeLater }
        val frame = JFrame("Habit Garden").apply { defaultCloseOperation = JFrame.EXIT_ON_CLOSE; setSize(850, 650) }; val root = JPanel(BorderLayout(12, 12)).apply { border = BorderFactory.createEmptyBorder(18, 18, 18, 18) }; frame.contentPane = root
        val list = JList<String>(); val model = DefaultListModel<String>(); list.model = model; val garden = Garden({ habits }, { list.selectedIndex }); root.add(garden, BorderLayout.NORTH); root.add(JScrollPane(list), BorderLayout.CENTER)
        fun draw() { val index = list.selectedIndex; model.clear(); val today = LocalDate.now(); habits.forEach { model.addElement("${if (today in it.days) "✓" else "○"} ${it.name} · ${streak(it, today)} day streak · ${it.days.size} check-ins · ${stage(it)}") }; if (index in habits.indices) list.selectedIndex = index; garden.repaint() }
        fun change(action: () -> Unit) { val old = habits.map { Habit(it.name, it.days.toSortedSet()) }.toMutableList(); try { action(); save(path, habits); draw() } catch (e: Exception) { habits = old; draw(); JOptionPane.showMessageDialog(frame, e.message) } }
        val controls = JPanel(); root.add(controls, BorderLayout.SOUTH)
        fun button(name: String, action: () -> Unit) { controls.add(JButton(name).apply { addActionListener { action() } }) }
        button("Plant a habit") { val name = JOptionPane.showInputDialog(frame, "Habit name:")?.trim(); if (!name.isNullOrEmpty()) change { habits.add(Habit(name)) } }
        button("Check in today") { if (list.selectedIndex in habits.indices) change { habits[list.selectedIndex].days.add(LocalDate.now()) } }
        button("Undo today's check-in") { if (list.selectedIndex in habits.indices) change { habits[list.selectedIndex].days.remove(LocalDate.now()) } }
        list.addListSelectionListener { garden.repaint() }; draw(); frame.setLocationRelativeTo(null); frame.isVisible = true
    }
}
