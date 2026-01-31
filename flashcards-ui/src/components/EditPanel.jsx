import CreateFlashCard from "./CreateFlashCard";

export default function EditPanel({ item, type}) {
    if(type === "flashcard") return <CreateFlashCard initialData={item} />;
}