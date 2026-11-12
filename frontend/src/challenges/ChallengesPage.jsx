import { useState } from 'react';
import StaleCounter from './StaleCounter.jsx';
import StudentInspector from './StudentInspector.jsx';
import StudentRoster from './StudentRoster.jsx';
import QuickEnroll from './QuickEnroll.jsx';
import FilteredStudents from './FilteredStudents.jsx';

// Each component below contains deliberate bugs. Symptoms and progressive hints: DEBUGGING_CHALLENGES.md
const challenges = [
  { id: 'DBG-F01', title: 'Counter that cannot count', Component: StaleCounter },
  { id: 'DBG-F02', title: 'Inspector stuck on one student', Component: StudentInspector },
  { id: 'DBG-F03', title: 'Roster shows blanks and swaps notes', Component: StudentRoster },
  { id: 'DBG-F04', title: 'Quick enroll always fails', Component: QuickEnroll },
  { id: 'DBG-F05', title: 'Filter that never stops fetching', Component: FilteredStudents },
];

export default function ChallengesPage() {
  const [activeId, setActiveId] = useState(challenges[0].id);
  const active = challenges.find((challenge) => challenge.id === activeId);

  return (
    <section>
      <h2>Debugging challenges</h2>
      <p className="muted">
        Each panel is broken on purpose. Reproduce the symptom, form a hypothesis, then fix the file in
        src/challenges/. Backend challenges are JUnit tests: ./run-task.sh DBG-B01 etc.
      </p>
      <div className="tabs">
        {challenges.map((challenge) => (
          <button
            key={challenge.id}
            type="button"
            className={challenge.id === activeId ? 'tab active' : 'tab'}
            onClick={() => setActiveId(challenge.id)}
          >
            {challenge.id}
          </button>
        ))}
      </div>
      <div className="challenge-panel">
        <h3>
          {active.id}: {active.title}
        </h3>
        <active.Component />
      </div>
    </section>
  );
}
