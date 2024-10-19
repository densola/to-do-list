export default function Home() {
    return (
        <div id="root">
            {/* TODO - don't hardcode url*/}
            {/* <form action="http://localhost:8000/" method="post">
                <input name="txt" type="text" />
                <button type="submit">Submit</button>
                </form> */}
            <button className="btn-add">Add task</button>
            <MyTask name="Title for task" info="More details provided on task at hand."></MyTask>
        </div>
    );
}

function MyTask(task: { name: string, info: string }) {
    return (
        <div className="task">
            <div className="task__header">
                <input className="task__checkbox" type="checkbox" />
                <h5 className="task__title">{task.name}</h5>
            </div>
            <p className="task__desc">{task.info}</p>
        </div>
    )
}