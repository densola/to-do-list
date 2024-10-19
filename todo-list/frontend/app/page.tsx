export default function Home() {
    return (
        <div>
            {/* TODO - don't hardcode url*/}
            <form action="http://localhost:8000/" method="post">
                <input name="txt" type="text" />
                <button type="submit">Submit</button>
            </form>
        </div>
    );
}
