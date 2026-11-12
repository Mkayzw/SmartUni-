// A visible placeholder so unfinished screens still render. Delete it from a page once you finish that task.
export default function TodoPanel({ taskId, children }) {
  return (
    <div className="todo-panel">
      <strong>{taskId}</strong>
      <div>{children}</div>
    </div>
  );
}
