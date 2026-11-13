import { useEffect, useState } from 'react';

// Expected: "+3" adds three. "Start auto" adds one every second until stopped.
export default function StaleCounter() {
  const [count, setCount] = useState(0);
  const [running, setRunning] = useState(false);

  function addThree() {
    setCount(count + 1);
    setCount(count + 1);
    setCount(count + 1);
  }

  useEffect(() => {
    if (!running) {
      return undefined;
    }
    const intervalId = setInterval(() => {
      setCount(count + 1);
    }, 1000);
    return () => clearInterval(intervalId);
  }, [running]);

  return (
    <div>
      <p className="stat-value">{count}</p>
      <button type="button" onClick={addThree}>+3</button>{' '}
      <button type="button" onClick={() => setRunning(!running)}>{running ? 'Stop auto' : 'Start auto'}</button>{' '}
      <button type="button" onClick={() => setCount(0)}>Reset</button>
    </div>
  );
}
