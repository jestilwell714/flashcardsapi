import CreateFlashCard from "./CreateFlashCard";

export default function CreatePanel({ type}) {
    if(type === "flashcard") return <CreateFlashCard/>;
}