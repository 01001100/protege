package org.protege.editor.owl.model.search.lucene;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.search.SearchContext;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Author: Josef Hardi <josef.hardi@stanford.edu><br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/11/2015
 */
public abstract class AbstractLuceneIndexer {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractLuceneIndexer.class);

    private final Directory indexDirectory;

    private final Analyzer textAnalyzer;

    private IndexWriter writer;

    public AbstractLuceneIndexer() {
        this(new StandardAnalyzer());
    }

    public AbstractLuceneIndexer(Analyzer analyzer) {
        textAnalyzer = analyzer;
        indexDirectory = setupIndexDirectory();
    }

    public void start() throws IOException {
        if (writer == null) {
            IndexWriterConfig writerConfig = new IndexWriterConfig(textAnalyzer);
            writer = new IndexWriter(indexDirectory, writerConfig);
        }
    }

    public void restart() throws IOException {
        if (writer != null && writer.isOpen()) {
            writer.close();
        }
        IndexWriterConfig writerConfig = new IndexWriterConfig(textAnalyzer);
        writer = new IndexWriter(indexDirectory, writerConfig);
    }

    public void doIndex(final OWLEditorKit editorKit, IndexProgressListener listener) throws IOException {
        doIndexing(new SearchContext(editorKit), listener);
    }

    private void doIndexing(SearchContext context, IndexProgressListener listener) throws IOException {
        for (OWLOntology ontology : context.getOntologies()) {
            logger.info("... building index for " + ontology.getOntologyID());
            
            // Calculate first the total number of axioms that will get indexed
            int totalAxiomCount = ontology.getAxiomCount(AxiomType.DECLARATION) + ontology.getAxiomCount(AxiomType.ANNOTATION_ASSERTION);
            if (totalAxiomCount == 0) continue; // skip if zero
            
            // Start indexing
            int progress = 1;
            for (OWLEntity entity : ontology.getSignature(Imports.INCLUDED)) {
                writer.addDocument(createEntityDocument(entity, context));
                listener.fireIndexingProgressed(percentage(progress++, totalAxiomCount));
                for (OWLAnnotation annotation : EntitySearcher.getAnnotations(entity, ontology)) {
                    writer.addDocument(createAnnotationDocument(entity, annotation, context));
                    listener.fireIndexingProgressed(percentage(progress++, totalAxiomCount));
                }
            }
        }
    }

    public void doAdd(final OWLEditorKit editorKit, OWLOntology ontology, OWLEntity entity) throws IOException {
        doAdding(new SearchContext(editorKit), ontology, entity);
    }

    private void doAdding(SearchContext context, OWLOntology ontology, OWLEntity entity) throws IOException {
        writer.addDocument(createEntityDocument(entity, context));
        for (OWLAnnotation annotation : EntitySearcher.getAnnotations(entity, ontology)) {
            writer.addDocument(createAnnotationDocument(entity, annotation, context));
        }
    }

    public void doDelete(OWLEntity entity) throws IOException {
        doDeleting(entity);
    }

    private void doDeleting(OWLEntity entity) throws IOException {
        writer.deleteDocuments(new Term(IndexField.ENTITY_IRI, getEntityId(entity)));
    }

    private int percentage(int progress, int total) {
        return (progress * 100) / total;
    }

    protected abstract Directory setupIndexDirectory();

    protected Document createEntityDocument(OWLEntity entity, SearchContext context) {
        Document doc = new Document();
        doc.add(new TextField(IndexField.ENTITY_IRI, getEntityId(entity), Store.YES));
        doc.add(new TextField(IndexField.DISPLAY_NAME, getDisplayName(entity, context), Store.YES));
        doc.add(new StringField(IndexField.ENTITY_TYPE, getType(entity), Store.YES));
        return doc;
    }

    protected Document createAnnotationDocument(OWLEntity entity, OWLAnnotation annotation, SearchContext context) {
        Document doc = new Document();
        doc.add(new TextField(IndexField.ENTITY_IRI, getEntityId(entity), Store.YES));
        doc.add(new TextField(IndexField.DISPLAY_NAME, getDisplayName(entity, context), Store.YES));
        doc.add(new StringField(IndexField.ANNOTATION_IRI, getAnnotationId(annotation), Store.YES));
        doc.add(new TextField(IndexField.ANNOTATION_DISPLAY_NAME, getDisplayName(annotation.getProperty(), context), Store.YES));
        doc.add(new TextField(IndexField.ANNOTATION_TEXT, getAnnotationText(annotation, context), Store.YES));
        return doc;
    }

    public DirectoryReader getIndexReader() {
        try {
            return DirectoryReader.open(indexDirectory);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected String getEntityId(OWLEntity entity) {
        return entity.getIRI().toString();
    }

    protected String getDisplayName(OWLObject object, SearchContext context) {
        return context.getRendering(object);
    }

    protected String getType(OWLEntity entity) {
        return entity.getEntityType().getName();
    }

    protected String getAnnotationId(OWLAnnotation annotation) {
        return annotation.getProperty().getIRI().toString();
    }

    protected String getAnnotationText(OWLAnnotation annotation, SearchContext context) {
        return context.getStyledStringRendering(annotation).getString();
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    public interface IndexProgressListener {

        void fireIndexingProgressed(long progress);
    }
}
